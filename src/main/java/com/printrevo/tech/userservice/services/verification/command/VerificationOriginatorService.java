package com.printrevo.tech.userservice.services.verification.command;

import com.printrevo.tech.platform.decorator.CommandBaseService;
import com.printrevo.tech.userservice.entities.core.verification.constansts.OriginatingSource;
import com.printrevo.tech.userservice.services.verification.command.instructions.VerificationOriginatorCommand;

public abstract class VerificationOriginatorService
        extends CommandBaseService<VerificationOriginatorCommand, String> {
    public abstract OriginatingSource originatingSource();
}
