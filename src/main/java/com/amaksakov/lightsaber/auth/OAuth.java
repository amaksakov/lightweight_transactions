/*
 * Lightsaber - Lightweight Transactions Processing Engine
 * This is a money transfer support API.  You can find out more about it on the project  at [https://github.com/amaksakov/lightweight_transactions](https://github.com/amaksakov/lightweight_transactions)
 *
 * OpenAPI spec version: 0.0.1
 * Contact: amaksakov@gmail.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.amaksakov.lightsaber.auth;

import com.amaksakov.lightsaber.Pair;

import java.util.Map;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2020-01-26T12:26:01.127+01:00")
public class OAuth implements Authentication {
  private String accessToken;

  public String getAccessToken() {
    return accessToken;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  @Override
  public void applyToParams(List<Pair> queryParams, Map<String, String> headerParams) {
    if (accessToken != null) {
      headerParams.put("Authorization", "Bearer " + accessToken);
    }
  }
}
