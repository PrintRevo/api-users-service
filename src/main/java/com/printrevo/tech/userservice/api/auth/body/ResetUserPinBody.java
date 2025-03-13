package com.printrevo.tech.userservice.api.auth.body;

import com.printrevo.tech.commonsecurity.constants.PinStatus;
import com.printrevo.tech.starter.logbook.annotation.Sensitive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetUserPinBody {
    private String userRefId;

    @Sensitive
    private String pin;
    private PinStatus status = PinStatus.TEMPORARY;
}
