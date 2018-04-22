package com.github.ykrank.alipayxposed.app.data.db.dbmodel;

import android.support.annotation.Nullable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 原始账单详情数据库
 */
@Entity(nameInDb = "BillDetailsRaw")
public class BillDetailsRaw {
    @Id(autoincrement = true)
    @Nullable
    private Long id;

    /**
     * 交易订单ID
     */
    @Property(nameInDb = "TradeNo")
    @Index(unique = true)
    private String tradeNo;

    /**
     * 交易订单标题，一般为交易方名称
     */
    @Property(nameInDb = "Header")
    private String header;

    /**
     * 交易订单金额
     */
    @Property(nameInDb = "Price")
    private float price;

    /**
     * 交易订单金额
     */
    @Property(nameInDb = "Status")
    private String status;

    /**
     * 交易订单数据，里面为Map(String name, String value)。
     */
    @Property(nameInDb = "RawJson")
    private String rawJson;

    /**
     * 交易订单原始数据，里面为原始网页。只有提取数据失败或者字段重复时才不为空。
     */
    @Property(nameInDb = "RawHtml")
    private String rawHtml;

    @Generated(hash = 1642756403)
    public BillDetailsRaw(Long id, String tradeNo, String header, float price,
            String status, String rawJson, String rawHtml) {
        this.id = id;
        this.tradeNo = tradeNo;
        this.header = header;
        this.price = price;
        this.status = status;
        this.rawJson = rawJson;
        this.rawHtml = rawHtml;
    }

    @Generated(hash = 1124802879)
    public BillDetailsRaw() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTradeNo() {
        return this.tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getRawJson() {
        return this.rawJson;
    }

    public void setRawJson(String rawJson) {
        this.rawJson = rawJson;
    }

    public String getRawHtml() {
        return this.rawHtml;
    }

    public void setRawHtml(String rawHtml) {
        this.rawHtml = rawHtml;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BillDetailsRaw{" +
                "id=" + id +
                ", tradeNo='" + tradeNo + '\'' +
                ", header='" + header + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", rawJson='" + rawJson + '\'' +
                ", rawHtml='" + rawHtml + '\'' +
                '}';
    }
}