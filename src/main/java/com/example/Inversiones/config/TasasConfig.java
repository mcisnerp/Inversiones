package com.example.Inversiones.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Getter
@Setter
@Configuration
//@PropertySource("tasasConfiguration.properties")
public class TasasConfig {
    //@Value("${plazofijo.tasa.10mil}")
    private Double tasa10=0.03;
    //@Value("${plazofijo.tasa.50mil}")
    private Double tasa50=0.05;
    //@Value("${plazofijo.tasa.100mil}")
    private Double tasa100=0.07;

    @Override
    public String toString() {
        return  "Monto mayor a 10000= " + tasa10 *100+"%"+
                "\nMonto mayor a 50000= " + tasa50 *100+"%"+
                "\nMonto mayor a 100000= " + tasa100*100+"%";
    }
}
