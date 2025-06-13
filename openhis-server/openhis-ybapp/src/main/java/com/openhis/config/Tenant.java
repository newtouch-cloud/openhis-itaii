package com.openhis.config;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;

/**
 * 多租户信息表
 * @TableName sys_tenant
 */
//@TableName(value ="sys_tenant")
@Data
@Component
@PropertySource(value="classpath:/config/tenant-config.properties",encoding="UTF-8",ignoreResourceNotFound=true)
@ConfigurationProperties(prefix = "pr")
public class Tenant implements Serializable {
    /**
     * 租户编码
     */
    //@TableId
    private String id;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 开始时间
     */
    private Date beginDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 客戶端id
     */
    private String clientId;//医保用

    /**
     * 客戶端安全码
     */
    private String clientSecret;//医保用

    /**
     * 医保服务平台账号
     */
    private String username;//医保用

    /**
     * 医保服务平台密码
     */
    private String password;//医保用

    /**
     * 终端授权类型
     */
    private String grantType;//医保用

    /**
     * 终端授权范围
     */
    private String scope;//医保用

    /**
     * 公钥
     */
    private String cliPubKey;

    /**
     * 秘钥
     */
    private String cliPrvKey;//医保用

    /**
     * 服务端公钥
     */
    private String serverPubKey;

    /**
     * 定点医药机构编号
     */
    private String fixmedinsCode;//医保用

    /**
     * 定点医药机构名称
     */
    private String fixmedinsName;//医保用

    /**
     * 统筹区号
     */
    private String admvs;//医保用

    /**
     * 状态 1正常 0冻结
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新日期
     */
    private Date updateTime;

    // @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Tenant other = (Tenant) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getBeginDate() == null ? other.getBeginDate() == null : this.getBeginDate().equals(other.getBeginDate()))
            && (this.getEndDate() == null ? other.getEndDate() == null : this.getEndDate().equals(other.getEndDate()))
            && (this.getClientId() == null ? other.getClientId() == null : this.getClientId().equals(other.getClientId()))
            && (this.getClientSecret() == null ? other.getClientSecret() == null : this.getClientSecret().equals(other.getClientSecret()))
            && (this.getUsername() == null ? other.getUsername() == null : this.getUsername().equals(other.getUsername()))
            && (this.getPassword() == null ? other.getPassword() == null : this.getPassword().equals(other.getPassword()))
            && (this.getGrantType() == null ? other.getGrantType() == null : this.getGrantType().equals(other.getGrantType()))
            && (this.getScope() == null ? other.getScope() == null : this.getScope().equals(other.getScope()))
            && (this.getCliPubKey() == null ? other.getCliPubKey() == null : this.getCliPubKey().equals(other.getCliPubKey()))
            && (this.getCliPrvKey() == null ? other.getCliPrvKey() == null : this.getCliPrvKey().equals(other.getCliPrvKey()))
            && (this.getServerPubKey() == null ? other.getServerPubKey() == null : this.getServerPubKey().equals(other.getServerPubKey()))
            && (this.getFixmedinsCode() == null ? other.getFixmedinsCode() == null : this.getFixmedinsCode().equals(other.getFixmedinsCode()))
            && (this.getFixmedinsName() == null ? other.getFixmedinsName() == null : this.getFixmedinsName().equals(other.getFixmedinsName()))
            && (this.getAdmvs() == null ? other.getAdmvs() == null : this.getAdmvs().equals(other.getAdmvs()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getCreateBy() == null ? other.getCreateBy() == null : this.getCreateBy().equals(other.getCreateBy()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateBy() == null ? other.getUpdateBy() == null : this.getUpdateBy().equals(other.getUpdateBy()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getBeginDate() == null) ? 0 : getBeginDate().hashCode());
        result = prime * result + ((getEndDate() == null) ? 0 : getEndDate().hashCode());
        result = prime * result + ((getClientId() == null) ? 0 : getClientId().hashCode());
        result = prime * result + ((getClientSecret() == null) ? 0 : getClientSecret().hashCode());
        result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
        result = prime * result + ((getPassword() == null) ? 0 : getPassword().hashCode());
        result = prime * result + ((getGrantType() == null) ? 0 : getGrantType().hashCode());
        result = prime * result + ((getScope() == null) ? 0 : getScope().hashCode());
        result = prime * result + ((getCliPubKey() == null) ? 0 : getCliPubKey().hashCode());
        result = prime * result + ((getCliPrvKey() == null) ? 0 : getCliPrvKey().hashCode());
        result = prime * result + ((getServerPubKey() == null) ? 0 : getServerPubKey().hashCode());
        result = prime * result + ((getFixmedinsCode() == null) ? 0 : getFixmedinsCode().hashCode());
        result = prime * result + ((getFixmedinsName() == null) ? 0 : getFixmedinsName().hashCode());
        result = prime * result + ((getAdmvs() == null) ? 0 : getAdmvs().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getCreateBy() == null) ? 0 : getCreateBy().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateBy() == null) ? 0 : getUpdateBy().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", beginDate=").append(beginDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", clientId=").append(clientId);
        sb.append(", clientSecret=").append(clientSecret);
        sb.append(", username=").append(username);
        sb.append(", password=").append(password);
        sb.append(", grantType=").append(grantType);
        sb.append(", scope=").append(scope);
        sb.append(", cliPubKey=").append(cliPubKey);
        sb.append(", cliPrvKey=").append(cliPrvKey);
        sb.append(", serverPubKey=").append(serverPubKey);
        sb.append(", fixmedinsCode=").append(fixmedinsCode);
        sb.append(", fixmedinsName=").append(fixmedinsName);
        sb.append(", admvs=").append(admvs);
        sb.append(", status=").append(status);
        sb.append(", createBy=").append(createBy);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateBy=").append(updateBy);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}