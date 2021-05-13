package io.drake.im.common.domain.http.vo;

import io.drake.im.common.constant.RelationCmdEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2021/05/07/22:45
 *
 * @author : Drake
 * Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationVO {

    private String userA;
    private String userB;
    private String userNameA;
    private String userNameB;

    private RelationCmdEnum cmdType;
}
