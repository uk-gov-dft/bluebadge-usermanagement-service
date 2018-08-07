package uk.gov.dft.bluebadge.service.usermanagement.service;

import static uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException.Operation.DELETE;
import static uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException.Operation.RETRIEVE;
import static uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException.Operation.UPDATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.model.usermanagement.generated.Password;
import uk.gov.dft.bluebadge.service.client.messageservice.MessageApiClient;
import uk.gov.dft.bluebadge.service.client.messageservice.model.GenericMessageRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.NewUserRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetSuccessRequest;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.*;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.service.usermanagement.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.service.usermanagement.service.referencedata.ReferenceDataService;

@Service
@Transactional
@Slf4j
public class UserManagementService {

  private final UserManagementRepository userManagementRepository;
  private final MessageApiClient messageApiClient;
  private final String laWebappEmailLinkURI;
  private final SecurityUtils securityUtils;
  private final PasswordEncoder passwordEncoder;
  private ReferenceDataService referenceDataService;

  @Autowired
  UserManagementService(
      UserManagementRepository repository,
      MessageApiClient messageApiClient,
      @Value("${blue-badge.la-webapp.email-link-uri}") String laWebappEmailLinkURI,
      SecurityUtils securityUtils,
      PasswordEncoder passwordEncoder,
      ReferenceDataService referenceDataService) {
    this.userManagementRepository = repository;
    this.messageApiClient = messageApiClient;
    this.laWebappEmailLinkURI = laWebappEmailLinkURI;
    this.securityUtils = securityUtils;
    this.passwordEncoder = passwordEncoder;
    this.referenceDataService = referenceDataService;
  }

  public UserEntity retrieveUserById(UUID userId) {
    RetrieveUserByIdParams params = getRetrieveUserByIdParams(userId);
    Optional<UserEntity> userEntity = userManagementRepository.retrieveUserById(params);
    if (!userEntity.isPresent()) {
      log.info("Request to retrieve user params:{} that did not exist", params);
      throw new NotFoundException("user", RETRIEVE);
    }
    return userEntity.get();
  }

  /**
   * Create user entity.
   *
   * @param userEntity User to create.
   * @throws BadRequestException if validation fails.
   */
  public void createUser(UserEntity userEntity) {
    List<ErrorErrors> businessErrors = businessValidateUser(userEntity);
    if (null != businessErrors) {
      log.debug("Business validation failed for user:{}", userEntity.getName());
      throw new BadRequestException(businessErrors);
    }
    List<ErrorErrors> localAuthorityErrors = localAuthorityValidateUser(userEntity);
    if (null != localAuthorityErrors) {
      log.debug("Local authority validation failed for user:{}", userEntity.getName());
      throw new BadRequestException(localAuthorityErrors);
    }
    createInitialPassword(userEntity);
    userManagementRepository.createUser(userEntity);
    requestEmailLinkMessage(userEntity, (ue, el) -> buildNewUserRequestDetails(ue, el));
    log.debug("Created user {}", userEntity.getUuid());
  }

  private void createInitialPassword(UserEntity userEntity) {
    String initialPassword = passwordEncoder.encode(UUID.randomUUID().toString());
    userEntity.setPassword(initialPassword);
  }

  private void requestEmailLinkMessage(
      UserEntity userEntity,
      BiFunction<UserEntity, EmailLink, ? extends GenericMessageRequest> messageDetailsFunc) {
    EmailLink emailLink = createEmailLink(userEntity);
    GenericMessageRequest messageDetails = messageDetailsFunc.apply(userEntity, emailLink);
    messageApiClient.sendEmailLinkMessage(messageDetails);
    userManagementRepository.updateUserToInactive(
        UuidAuthorityCodeParams.builder().uuid(userEntity.getUuid()).build());
    log.debug("Successfully changed password for user:{}", userEntity.getUuid());
  }

  private PasswordResetRequest buildPasswordRequestDetails(UserEntity ue, EmailLink el) {
    return PasswordResetRequest.builder()
        .emailAddress(ue.getEmailAddress())
        .fullName(ue.getName())
        .passwordLink(el.getLink())
        .build();
  }

  private NewUserRequest buildNewUserRequestDetails(UserEntity ue, EmailLink el) {
    LocalAuthorityEntity localAuthority =
        LocalAuthorityEntity.builder()
            .code(ue.getLocalAuthorityId())
            .name(referenceDataService.getLocalAuthorityName(ue.getLocalAuthorityId()))
            .build();
    return NewUserRequest.builder()
        .emailAddress(ue.getEmailAddress())
        .fullName(ue.getName())
        .passwordLink(el.getLink())
        .localAuthority(localAuthority.getName())
        .build();
  }

  private EmailLink createEmailLink(UserEntity userEntity) {
    EmailLink emailLink =
        EmailLink.builder()
            .webappUri(this.laWebappEmailLinkURI)
            .uuid(UUID.randomUUID().toString())
            .userUuid(userEntity.getUuid())
            .build();
    userManagementRepository.createEmailLink(emailLink);
    return emailLink;
  }

  /**
   * Requests reset of a users password via message service.
   *
   * @param userId The user PK.
   */
  public void requestPasswordResetEmail(UUID userId) {
    log.debug("Resetting password for user:{}", userId);
    RetrieveUserByIdParams params = getRetrieveUserByIdParams(userId);
    Optional<UserEntity> optionalUserEntity = userManagementRepository.retrieveUserById(params);
    UserEntity userEntity;
    if (optionalUserEntity.isPresent()) {
      userEntity = optionalUserEntity.get();
    } else {
      throw new NotFoundException("user", RETRIEVE);
    }
    requestEmailLinkMessage(userEntity, (ue, el) -> buildPasswordRequestDetails(ue, el));
  }

  /**
   * Delete a user.
   *
   * @param id PK of user to delete.
   */
  public void deleteUser(UUID id) {
    DeleteUserParams params = getDeleteUserParams(id);
    if (userManagementRepository.deleteUser(params) == 0) {
      throw new NotFoundException("user", DELETE);
    }
  }

  public List<UserEntity> retrieveUsersByAuthorityId(String authorityId, String nameFilter) {
    if (null != nameFilter) {
      nameFilter = "%" + nameFilter + "%";
    }
    UserEntity queryParams = new UserEntity();
    queryParams.setLocalAuthorityId(authorityId);
    queryParams.setName(nameFilter);
    queryParams.setEmailAddress(nameFilter);
    return userManagementRepository.findUsers(queryParams);
  }

  /**
   * Update user entity.
   *
   * @param userEntity Entity to update.
   * @throws BadRequestException if validation fails.
   */
  public void updateUser(UserEntity userEntity) {
    List<ErrorErrors> businessErrors = businessValidateUser(userEntity);
    if (null != businessErrors) {
      throw new BadRequestException(businessErrors);
    }
    List<ErrorErrors> localAuthorityErrors = localAuthorityValidateUser(userEntity);
    if (null != localAuthorityErrors) {
      log.debug("Local authority validation failed for user:{}", userEntity.getName());
      throw new BadRequestException(localAuthorityErrors);
    }

    if (userManagementRepository.updateUser(userEntity) == 0) {
      throw new NotFoundException("user", UPDATE);
    }
    log.debug("Updated user {}", userEntity.getUuid());
  }

  /** Update user password. */
  public void updatePassword(String uuid, Password passwords) {

    log.debug("Updating password for guid:{}", uuid);
    String password = passwords.getPassword();
    String passwordConfirm = passwords.getPasswordConfirm();

    if (!password.equals(passwordConfirm)) {
      log.debug("Passwords did not match. {}", uuid);
      ErrorErrors error = new ErrorErrors();
      error.setField("passwordConfirm");
      error.setMessage("Pattern.password.passwordConfirm");
      error.setReason("Password confirm field does not match with password field");
      throw new BadRequestException(error);
    }

    EmailLink link = this.userManagementRepository.retrieveEmailLinkWithUuid(uuid);

    if (link == null || !link.getIsActive()) {
      ErrorErrors error = new ErrorErrors();
      error.setField("password");
      if (link == null) {
        error.setMessage("Invalid.uuid");
      } else {
        error.setMessage("Inactive.uuid");
      }
      error.setReason("uuid is not valid");
      throw new BadRequestException(error);
    }

    String hash = passwordEncoder.encode(password);
    UserEntity user = new UserEntity();
    user.setUuid(link.getUserUuid());
    user.setPassword(hash);

    userManagementRepository.updateEmailLinkToInvalid(uuid);
    log.debug("Passwords updated. {}", uuid);
    if (userManagementRepository.updatePassword(user) == 0) {
      throw new NotFoundException("user", UPDATE);
    }
    RetrieveUserByIdParams params =
        RetrieveUserByIdParams.builder().userId(link.getUserUuid()).build();
    Optional<UserEntity> userEntity = userManagementRepository.retrieveUserById(params);
    PasswordResetSuccessRequest passwordResetSuccessRequest =
        PasswordResetSuccessRequest.builder()
            .emailAddress(userEntity.get().getEmailAddress())
            .fullName(userEntity.get().getName())
            .build();
    messageApiClient.sendPasswordResetSuccessMessage(passwordResetSuccessRequest);
  }

  public UserEntity retrieveUserUsingUuid(String uuid) {

    Optional<UserEntity> userEntity = userManagementRepository.retrieveUserUsingEmailLinkUuid(uuid);
    if (!userEntity.isPresent()) {
      throw new NotFoundException("user", RETRIEVE);
    }
    return userEntity.get();
  }

  private RetrieveUserByIdParams getRetrieveUserByIdParams(UUID userId) {
    String localAuthority = securityUtils.getCurrentLocalAuthority().getShortCode();
    return RetrieveUserByIdParams.builder().userId(userId).localAuthority(localAuthority).build();
  }

  private DeleteUserParams getDeleteUserParams(UUID userUuid) {
    String localAuthority = securityUtils.getCurrentLocalAuthority().getShortCode();
    return DeleteUserParams.builder().userUuid(userUuid).localAuthority(localAuthority).build();
  }

  /**
   * Apply business validation (non bean).
   *
   * @param userEntity User Entity bean.
   * @return List of errors or null if validation ok.
   */
  private List<ErrorErrors> businessValidateUser(UserEntity userEntity) {
    List<ErrorErrors> errorsList = null;

    if (userManagementRepository.emailAddressAlreadyUsed(userEntity)) {
      ErrorErrors error = new ErrorErrors();
      error
          .field("emailAddress")
          .message("AlreadyExists.user.emailAddress")
          .reason("Email Address already used.");
      errorsList = new ArrayList<>();
      errorsList.add(error);
    }

    return errorsList;
  }

  /**
   * Checks if current user's local authority is same as userEntity's.
   *
   * @param userEntity User Entity bean.
   * @return List of errors or null if validation ok.
   */
  private List<ErrorErrors> localAuthorityValidateUser(UserEntity userEntity) {
    List<ErrorErrors> errorsList = null;

    String localAuthority = securityUtils.getCurrentLocalAuthority().getShortCode();
    if (localAuthority != userEntity.getLocalAuthorityId()) {
      ErrorErrors error = new ErrorErrors();
      error
          .field("localAuthority")
          .message("NotSameAsCurrentUsers.user.localAuthority")
          .reason("Current user's local authority does not match userEntity's.");
      errorsList = new ArrayList<>();
      errorsList.add(error);
    }

    return errorsList;
  }
}
