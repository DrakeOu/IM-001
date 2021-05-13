package io.drake.im.restweb.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Date: 2021/04/22/16:02
 *
 * @author : Drake
 * Description:
 */
@Entity(name = "chat_msg")
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMsg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String origin;
    private String dest;
    private String content;
    @Column(name = "is_signed")
    private Boolean isSigned;
    @Column(name = "create_time")
    private Date createTime;


}
