package uk.gov.dft.bluebadge.service.usermanagement.service;

import static java.time.temporal.ChronoUnit.HOURS;
import static uk.gov.dft.bluebadge.common.service.exception.NotFoundException.Operation.DELETE;
import static uk.gov.dft.bluebadge.common.service.exception.NotFoundException.Operation.RETRIEVE;
import static uk.gov.dft.bluebadge.common.service.exception.NotFoundException.Operation.UPDATE;

import java.time.Instant;
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
import org.springframework.util.StringUtils;
import uk.gov.dft.bluebadge.common.api.model.ErrorErrors;
import uk.gov.dft.bluebadge.common.security.Role;
import uk.gov.dft.bluebadge.common.security.SecurityUtils;
import uk.gov.dft.bluebadge.common.service.exception.BadRequestException;
import uk.gov.dft.bluebadge.common.service.exception.NotFoundException;
import uk.gov.dft.bluebadge.model.usermanagement.generated.Password;
import uk.gov.dft.bluebadge.service.client.messageservice.MessageApiClient;
import uk.gov.dft.bluebadge.service.client.messageservice.model.GenericMessageRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.NewUserRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetRequest;
import uk.gov.dft.bluebadge.service.client.messageservice.model.PasswordResetSuccessRequest;
import uk.gov.dft.bluebadge.service.usermanagement.repository.UserManagementRepository;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.EmailLink;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UserEntity;
import uk.gov.dft.bluebadge.service.usermanagement.repository.domain.UuidAuthorityCodeParams;
import uk.gov.dft.bluebadge.service.usermanagement.service.referencedata.ReferenceDataService;

@Service
@Transactional
@Slf4j
public class UserManagementService {

  public static final String LOCAL_AUTHORITY_SHORT_CODE = "localAuthorityShortCode";
  public static final String EMAIL_ADDRESS = "emailAddress";
  public static final Long LINK_TIMEOUT_HOURS = 24L;
  private final UserManagementRepository userManagementRepository;
  private final MessageApiClient messageApiClient;
  private final String laWebappEmailLinkURI;
  private final SecurityUtils securityUtils;
  private final PasswordEncoder passwordEncoder;
  private final ReferenceDataService referenceDataService;
  private final CommonPasswordsFilter passwordFilter;

  @Autowired
  UserManagementService(
      UserManagementRepository repository,
      MessageApiClient messageApiClient,
      @Value("${blue-badge.la-webapp.email-link-uri}") String laWebappEmailLinkURI,
      SecurityUtils securityUtils,
      PasswordEncoder passwordEncoder,
      ReferenceDataService referenceDataService,
      CommonPasswordsFilter passwordFilter) {
    this.userManagementRepository = repository;
    this.messageApiClient = messageApiClient;
    this.laWebappEmailLinkURI = laWebappEmailLinkURI;
    this.securityUtils = securityUtils;
    this.passwordEncoder = passwordEncoder;
    this.referenceDataService = referenceDataService;
    this.passwordFilter = passwordFilter;
  }

  public UserEntity retrieveUserById(UUID userUuid) {
    UuidAuthorityCodeParams params = getUuidAuthorityCodeParams(userUuid);
    Optional<UserEntity> userEntity = userManagementRepository.retrieveUserByUuid(params);
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
    if (!businessErrors.isEmpty()) {
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
    requestEmailLinkMessage(userEntity, this::buildNewUserRequestDetails);
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
    log.debug("Successfully sent  ***REMOVED***, userEntity.getUuid());
  }

  private PasswordResetRequest buildPasswordRequestDetails(UserEntity ue, EmailLink el) {
    return PasswordResetRequest.builder()
        .emailAddress(ue.getEmailAddress())
        .fullName(ue.getName())
        .passwordLink(el.getLink())
        .build();
  }

  private NewUserRequest buildNewUserRequestDetails(UserEntity ue, EmailLink el) {
    return NewUserRequest.builder()
        .emailAddress(ue.getEmailAddress())
        .fullName(ue.getName())
        .passwordLink(el.getLink())
        .localAuthority(
            ue.getAuthorityCode() == null
                ? "DfT"
                : referenceDataService.getLocalAuthorityName(ue.getAuthorityCode()))
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
   * @param userUuid The user PK.
   */
  public void requestPasswordResetEmail(UUID userUuid) {
    log.debug("Resetting password for user:{}", userUuid);
    UuidAuthorityCodeParams params = getUuidAuthorityCodeParams(userUuid);
    Optional<UserEntity> optionalUserEntity = userManagementRepository.retrieveUserByUuid(params);
    UserEntity userEntity;
    if (optionalUserEntity.isPresent()) {
      userEntity = optionalUserEntity.get();
    } else {
      throw new NotFoundException("user", RETRIEVE);
    }
    requestEmailLinkMessage(userEntity, this::buildPasswordRequestDetails);
  }

  /**
   * Delete a user.
   *
   * @param uuid PK of user to delete.
   */
  public void deleteUser(UUID uuid) {
    UuidAuthorityCodeParams params = getUuidAuthorityCodeParams(uuid);
    if (userManagementRepository.deleteUser(params) == 0) {
      throw new NotFoundException("user", DELETE);
    }
  }

  public List<UserEntity> findUsers(String nameFilter) {
    if (null != nameFilter) {
      nameFilter = "%" + nameFilter + "%";
    }
    UserEntity queryParams = new UserEntity();
    queryParams.setAuthorityCode(securityUtils.getCurrentLocalAuthorityShortCode());
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
    if (!businessErrors.isEmpty()) {
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

    passwordFilter.validatePasswordBlacklisted(passwords.getPassword());

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

    if (link == null
        || !link.getIsActive()
        || link.getCreatedOn().isBefore(Instant.now().minus(LINK_TIMEOUT_HOURS, HOURS))) {
      ErrorErrors error = new ErrorErrors();
      error.setField("password");
      error.setReason("uuid is not valid");
      if (link == null) {
        error.setMessage("email.link.invalid.uuid");
      } else if (!link.getIsActive()) {
        error.setMessage("email.link.inactive.uuid");
      } else {
        error.setMessage("email.link.expired.uuid");
      }
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
    UuidAuthorityCodeParams params =
        UuidAuthorityCodeParams.builder().uuid(link.getUserUuid()).build();
    Optional<UserEntity> userEntity = userManagementRepository.retrieveUserByUuid(params);
    // The user entity really should be present as we just updated the password.
    if (userEntity.isPresent()) {
      PasswordResetSuccessRequest passwordResetSuccessRequest =
          PasswordResetSuccessRequest.builder()
              .emailAddress(userEntity.get().getEmailAddress())
              .fullName(userEntity.get().getName())
              .build();
      messageApiClient.sendPasswordResetSuccessMessage(passwordResetSuccessRequest);
    }
  }

  public UserEntity retrieveUserUsingUuid(String uuid) {

    Optional<UserEntity> userEntity = userManagementRepository.retrieveUserUsingEmailLinkUuid(uuid);
    if (!userEntity.isPresent()) {
      throw new NotFoundException("user", RETRIEVE);
    }
    return userEntity.get();
  }

  public UserEntity retrieveCurrentUser() {
    String emailAddress = securityUtils.getCurrentAuth().getEmailAddress();
    if (StringUtils.isEmpty(emailAddress)) {
      ErrorErrors error = new ErrorErrors();
      error.setField("access token");
      error.setMessage("Access token not associated with a user.");
      error.setReason("No email address associated with the access token");
      throw new BadRequestException(error);
    }

    UserEntity searchParams = new UserEntity();
    searchParams.setEmailAddress(emailAddress);
    searchParams.setName(UUID.randomUUID().toString());
    return userManagementRepository
        .findUsers(searchParams)
        .stream()
        .findFirst()
        .orElseThrow(() -> new NotFoundException("user", RETRIEVE));
  }

  private UuidAuthorityCodeParams getUuidAuthorityCodeParams(UUID userUuid) {
    return UuidAuthorityCodeParams.builder().uuid(userUuid).build();
  }

  /**
   * Apply business validation (non bean).
   *
   * @param userEntity User Entity bean.
   * @return List of errors or null if validation ok.
   */
  private List<ErrorErrors> businessValidateUser(UserEntity userEntity) {
    List<ErrorErrors> errorsList = new ArrayList<>();

    // Validate authority short code valid
    if (null == userEntity.getAuthorityCode()) {
      if (Role.DFT_ADMIN.getRoleId() != userEntity.getRoleId()) {
        ErrorErrors error = new ErrorErrors();
        error
            .field(LOCAL_AUTHORITY_SHORT_CODE)
            .message("NotNull.user.localAuthorityShortCode")
            .reason(
                "Local authority is mandatory for permission: "
                    + Role.getById(userEntity.getRoleId()));

        errorsList.add(error);
      }
    } else if (!referenceDataService.isValidLocalAuthorityCode(userEntity.getAuthorityCode())) {
      ErrorErrors error = new ErrorErrors();
      error
          .field(LOCAL_AUTHORITY_SHORT_CODE)
          .message("Invalid.user.localAuthorityShortCode")
          .reason("Could not find local authority for short code " + userEntity.getAuthorityCode());

      errorsList.add(error);
    }

    if (userManagementRepository.emailAddressAlreadyUsed(userEntity)) {
      ErrorErrors error = new ErrorErrors();
      error
          .field(EMAIL_ADDRESS)
          .message("AlreadyExists.user.emailAddress")
          .reason("Email Address already used.");

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

    String localAuthority = securityUtils.getCurrentLocalAuthorityShortCode();
    if (localAuthority != null && !localAuthority.equals(userEntity.getAuthorityCode())) {
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
