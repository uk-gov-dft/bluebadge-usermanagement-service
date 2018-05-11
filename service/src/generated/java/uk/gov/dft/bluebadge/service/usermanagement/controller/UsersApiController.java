package uk.gov.dft.bluebadge.service.usermanagement.controller;

import org.springframework.stereotype.Controller;
import java.util.Optional;

@Controller
public class UsersApiController implements UsersApi {

    private final UsersApiDelegate delegate;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(UsersApiDelegate delegate) {
        this.delegate = delegate;
    }

    @Override
    public UsersApiDelegate getDelegate() {
        return delegate;
    }
}
