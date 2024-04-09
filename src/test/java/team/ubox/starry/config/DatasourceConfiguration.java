package team.ubox.starry.config;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

@Configuration
public class DatasourceConfiguration {
    private final EmbeddedPostgres embeddedPostgres;

    public DatasourceConfiguration() throws IOException, SQLException {
        embeddedPostgres = EmbeddedPostgres.start();
        ScriptUtils.executeSqlScript(embeddedPostgres.getPostgresDatabase().getConnection(), new ClassPathResource("schema.sql"));
    }

    @Bean
    public DataSource dataSource() {
        return embeddedPostgres.getPostgresDatabase();
    }
}
