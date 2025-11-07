package com.algaworks.algashop.ordering.infrastructure.utility.modelmapper;

import com.algaworks.algashop.ordering.application.utility.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NamingConventions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public Mapper mapper() {
        ModelMapper modelMapper = new ModelMapper();
        
        configuration(modelMapper);

        return new Mapper() {

            @Override
            public <T> T convert(Object object, Class<T> targetClass) {
                return modelMapper.map(object, targetClass);
            }
        };
    }

    /**
     * Essa configuração ajusta como o ModelMapper faz o matching entre propriedades de origem e destino:
     *
     * setSourceNamingConvention(NamingConventions.NONE)
     * Desativa transformações de convenção de nomes na origem. O mapeador não tentará converter entre estilos (por exemplo,
     * camelCase ↔ snake_case).
     *
     * setDestinationNamingConvention(NamingConventions.NONE)
     * Mesma coisa para a destinação — exige que nomes sejam usados literalmente, sem heurísticas de conversão.
     *
     * setMatchingStrategy(MatchingStrategies.STRICT)
     * Define a estratégia de correspondência como estrita: somente propriedades com nomes e tipos compatíveis de forma clara
     * serão mapeadas automaticamente. Evita mapeamentos "aproximados" ou heurísticos que podem causar resultados inesperados.
     *
     * Consequência prática: mapeamentos ficam mais seguros e previsíveis, mas você precisa garantir que os nomes e tipos dos
     * campos entre origem e destino sejam compatíveis ou então fornecer mapeamentos explícitos/conversores.
     */
    private void configuration(ModelMapper modelMapper) {

        modelMapper.getConfiguration()
                .setSourceNamingConvention(NamingConventions.NONE)
                .setDestinationNamingConvention(NamingConventions.NONE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

}
