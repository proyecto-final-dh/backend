package com.company.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
  private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

  private static Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt) throws JsonProcessingException {
    Set<GrantedAuthority> resourcesRoles = new HashSet();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    resourcesRoles.addAll(extractRoles("resource_access", objectMapper.readTree(objectMapper.writeValueAsString(jwt)).get("claims")));
    resourcesRoles.addAll(extractRolesRealmAccess("realm_access", objectMapper.readTree(objectMapper.writeValueAsString(jwt)).get("claims")));
    resourcesRoles.addAll(extractIdUser("sub", objectMapper.readTree(objectMapper.writeValueAsString(jwt)).get("claims")));
    return resourcesRoles;
  }


  private static List<GrantedAuthority> extractRoles(String route, JsonNode jwt) {
    Set<String> rolesWithPrefix = new HashSet<>();

    jwt.path(route)
            .elements()
            .forEachRemaining(e -> e.path("roles")
                    .elements()
                    .forEachRemaining(r -> rolesWithPrefix.add("ROLE_" + r.asText())));

    final List<GrantedAuthority> authorityList =
            AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));

    return authorityList;
  }
  private static List<GrantedAuthority> extractRolesRealmAccess(String route, JsonNode jwt) {
    Set<String> rolesWithPrefix = new HashSet<>();

    jwt.path(route)
            .path("roles")
            .elements()
            .forEachRemaining(r -> rolesWithPrefix.add("ROLE_" + r.asText()));

    final List<GrantedAuthority> authorityList =
            AuthorityUtils.createAuthorityList(rolesWithPrefix.toArray(new String[0]));

    return authorityList;
  }

  private static List<GrantedAuthority> extractIdUser(String route,JsonNode jwt) {
    // Extraer el valor del campo 'sub'.
    String userId = jwt.path("sub").asText();

    // Crear la lista de GrantedAuthority con el ID del usuario.
    GrantedAuthority authority = new SimpleGrantedAuthority("ID_" + userId);

    // Devolver una lista con esa única autoridad.
    return Collections.singletonList(authority);
  }



  public JwtAuthenticationConverter() {
  }


  public AbstractAuthenticationToken convert(final Jwt source) {
    Collection<GrantedAuthority> authorities = null;
    try {
      authorities = this.getGrantedAuthorities(source);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return new JwtAuthenticationToken(source, authorities);
  }

  public Collection<GrantedAuthority> getGrantedAuthorities(Jwt source) throws JsonProcessingException {
  var prueba= this.defaultGrantedAuthoritiesConverter.convert(source).stream();
    return (Collection) Stream.concat(prueba, extractResourceRoles(source).stream()).collect(Collectors.toSet());
  }
}
