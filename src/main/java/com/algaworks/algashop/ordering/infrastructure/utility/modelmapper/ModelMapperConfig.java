package com.algaworks.algashop.ordering.infrastructure.utility.modelmapper;

import com.algaworks.algashop.ordering.application.customer.management.CustomerOutput;
import com.algaworks.algashop.ordering.application.utility.Mapper;
import com.algaworks.algashop.ordering.domain.model.commons.FullName;
import com.algaworks.algashop.ordering.domain.model.customer.BirthDate;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NamingConventions;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class ModelMapperConfig {

    private static final Converter<FullName, String> fullNameToFirstNameConverter = new Converter<FullName, String>() {

                @Override
                public String convert(MappingContext<FullName, String> mappingContext) {
                    FullName fullName = mappingContext.getSource();

                    if (fullName == null) {
                        return null;
                    }

                    return fullName.firstName();
                }
            };

    private static final Converter<FullName, String> fullNameToLastNameConverter = new Converter<FullName, String>() {

        @Override
        public String convert(MappingContext<FullName, String> mappingContext) {
            FullName fullName = mappingContext.getSource();

            if (fullName == null) {
                return null;
            }

            return fullName.lastName();
        }
    };

    private static final Converter<BirthDate, LocalDate> birthDateConverter = new Converter<BirthDate, LocalDate>() {

        @Override
        public LocalDate convert(MappingContext<BirthDate, LocalDate> mappingContext) {
            BirthDate birthDate = mappingContext.getSource();

            if (birthDate == null) {
                return null;
            }

            return birthDate.value();
        }
    };

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

        modelMapper.createTypeMap(Customer.class, CustomerOutput.class)
                .addMappings(mapping -> mapping.using(fullNameToFirstNameConverter)
                        .map(Customer::fullName, CustomerOutput::setFirstName)
                ).addMappings(mapping -> mapping.using(fullNameToLastNameConverter)
                        .map(Customer::fullName, CustomerOutput::setLastName)
                ).addMappings(mapping -> mapping.using(birthDateConverter)
                        .map(Customer::birthDate, CustomerOutput::setBirthDate)
                );
    }

}
