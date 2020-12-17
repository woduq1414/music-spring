package dimigo.test.MusicSpring;


import dimigo.test.MusicSpring.repository.JPAMusicRepository;
import dimigo.test.MusicSpring.repository.MusicRepository;
import dimigo.test.MusicSpring.service.MusicService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Configuration
public class SpringConfigure {

    private  DataSource dataSource;
    private  EntityManager em;

    public SpringConfigure(DataSource dataSource, EntityManager em) {
        this.dataSource = dataSource;
        this.em = em;
    }

    @Bean
    public MusicService musicService()
    {
        return new MusicService(musicRepository());
    }
    @Bean
    public MusicRepository musicRepository()
    {
        //return new MemoryMemberRepository();
        //return new JDBCMemberRepository(dataSource);
        return new JPAMusicRepository(em);
    }

}


@Configuration
class MyDataSourceConfig {

    @Bean
    @Primary
    public DataSource getDataSource() {
        String hostName = null;
        try {
            System.out.println(InetAddress.getLocalHost().getHostName());
            hostName = InetAddress.getLocalHost().getHostName();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        String url = null;
        String username = null;
        String password = null;
        String driverClassName = null;

        if(hostName.equals("DESKTOP-32A6L4O")){
            url = "jdbc:mysql://localhost:3306/dimigo?useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
            username = "root";
            password = "qwe123";
            driverClassName = "com.mysql.cj.jdbc.Driver";
        }else{
            url = System.getenv("DB_URL");
            username = System.getenv("DB_USERNAME");
            password = System.getenv("DB_PASSWORD");
            driverClassName = "com.mysql.cj.jdbc.Driver";
        }



        /*
         * Create the datasource and return it
         *
         * You could create the specific DS
         * implementation (ie: org.postgresql.ds.PGPoolingDataSource)
         * or ask Spring's DataSourceBuilder to autoconfigure it for you,
         * whichever works best in your eyes
         */

        return DataSourceBuilder
                .create()
                .url( url )
                .username( username )
                .password( password )
                .driverClassName( driverClassName )
                .build();
    }
}