package com.mmall.param;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liliang
 * @date 2017/11/16.
 */
@Getter
@Setter
public class TestVo {

    @NotBlank
    private String msg;

    @NotNull(message = "id 不能为空")
    @Max(value = 10,message = "id 不能大于10")
    @Min( value = 0,message = "id不能小于0")
    private Integer id;

    @NotEmpty
    private List<String > str;
}
