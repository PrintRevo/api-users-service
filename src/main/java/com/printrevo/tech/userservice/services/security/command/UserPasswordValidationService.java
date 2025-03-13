package com.printrevo.tech.userservice.services.security.command;

import com.printrevo.tech.platform.api.ResultStatus;
import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.platform.exceptions.ClientErrorException;
import com.printrevo.tech.platform.result.Result;
import com.printrevo.tech.userservice.services.security.command.instructions.UserPasswordValidationCommand;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class UserPasswordValidationService
        extends CommandBaseService<UserPasswordValidationCommand, Void> {

    public static void isValidPassword(String password) {
        int passwordLength = password.length();
        if (passwordLength > 20 || passwordLength < 8) {
            throw new ClientErrorException("Password must be between 8 and 20 characters in length.");
        }

        int upperCaseCount = 0;
        int lowerCaseCount = 0;
        int digitCount = 0;
        int specialCharCount = 0;
        for (int i = 0; i < passwordLength; i++) {
            char c = password.charAt(i);
            if (Character.isUpperCase(c)) {
                upperCaseCount++;
            } else if (Character.isLowerCase(c)) {
                lowerCaseCount++;
            } else if (Character.isDigit(c)) {
                digitCount++;
            } else if (c == '@' || c == '#' || c == '$' || c == '%') {
                specialCharCount++;
            }
        }

        validateUpperCaseCount(upperCaseCount);
        validateLowerCaseCount(lowerCaseCount);
        validateDigitCount(digitCount);
        validateSpecialCharCount(specialCharCount);
    }

    private static void validateSpecialCharCount(int specialCharCount) {
        if (specialCharCount == 0) {
            throw new ClientErrorException("Password must contain at least one special character (@, #, $, %).");
        }
    }

    private static void validateDigitCount(int digitCount) {
        if (digitCount == 0) {
            throw new ClientErrorException("Password must contain at least one digit.");
        }
    }

    private static void validateLowerCaseCount(int lowerCaseCount) {
        if (lowerCaseCount == 0) {
            throw new ClientErrorException("Password must contain at least one lowercase letter.");
        }
    }

    private static void validateUpperCaseCount(int upperCaseCount) {
        if (upperCaseCount == 0) {
            throw new ClientErrorException("Password must contain at least one uppercase letter.");
        }
    }

    @Override
    public Result<Void> execute(UserPasswordValidationCommand command) {

        try {
            isValidPassword(command.getPassword());
            return new Result.ResultBuilder<Void>()
                    .build();
        } catch (HttpClientErrorException e) {
            return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(e.getStatusCode(), e.getLocalizedMessage()))
                    .build();
        } catch (Exception e) {
            return new Result.ResultBuilder<Void>()
                    .withStatus(new ResultStatus(HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage()))
                    .build();
        }
    }
}
