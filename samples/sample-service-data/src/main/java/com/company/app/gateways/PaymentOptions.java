package com.company.app.gateways;

import io.soffa.foundation.core.models.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOptions implements Model {

    //@JsonProperty("mobile_money")
    private MobileMoneyOption mobileMoney;

}
