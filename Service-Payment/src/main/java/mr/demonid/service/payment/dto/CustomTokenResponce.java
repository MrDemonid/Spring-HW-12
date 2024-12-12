package mr.demonid.service.payment.dto;

import lombok.Data;

@Data
public class CustomTokenResponce {
    private String access_token;
    private String scope;
    private String token_type;
    private int expires_in;
}
