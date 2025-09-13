package co.com.zenway.security.adapter;

import co.com.zenway.model.rol.RoleType;
import co.com.zenway.model.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;


    public String generarToken(Usuario usuario) {
        Instant now = Instant.now();
        long expiracion = 36000L;

        RoleType roleType = RoleType.fromId(usuario.getRolId());

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("auth-service")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiracion))
                .subject(usuario.getId().toString())
                .claim("roles", List.of(roleType.name()))
                .build();

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claimsSet)).getTokenValue();

    }


}


