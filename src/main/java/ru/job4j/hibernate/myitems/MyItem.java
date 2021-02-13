package ru.job4j.hibernate.myitems;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "myitem")
@Data
@NoArgsConstructor
@EqualsAndHashCode()
public class MyItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @EqualsAndHashCode.Include
    private String description;

    @JsonSerialize(using = CustomDateSerializer.class)
    private LocalDateTime created;

    private Boolean done;

    public MyItem(String description) {
        this.description = description;
        this.created = LocalDateTime.now();
        this.done = false;
    }

    static class CustomDateSerializer extends StdSerializer<LocalDateTime> {

        private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        public CustomDateSerializer() {
            this(null);
        }

        public CustomDateSerializer(Class t) {
            super(t);
        }

        @Override
        public void serialize(LocalDateTime localDateTime,
                              JsonGenerator jsonGenerator,
                              SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(localDateTime.format(formatter));
        }
    }
}
