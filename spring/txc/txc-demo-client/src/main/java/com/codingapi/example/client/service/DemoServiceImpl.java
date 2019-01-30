package com.codingapi.example.client.service;import com.codingapi.example.client.mapper.ClientDemoMapper;import com.codingapi.example.common.db.domain.Demo;import com.codingapi.example.common.spring.DDemoClient;import com.codingapi.example.common.spring.EDemoClient;import com.codingapi.txlcn.tc.core.DTXLocalContext;import com.codingapi.txlcn.tc.annotation.TxcTransaction;import lombok.extern.slf4j.Slf4j;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.beans.factory.annotation.Value;import org.springframework.core.annotation.Order;import org.springframework.stereotype.Service;import org.springframework.transaction.annotation.Transactional;import java.util.Date;/** * Description: * Date: 2018/12/25 * * @author ujued */@Service@Order(Integer.MIN_VALUE)@Slf4jpublic class DemoServiceImpl implements DemoService {    private final ClientDemoMapper demoMapper;    private final DDemoClient dDemoClient;    private final EDemoClient eDemoClient;    @Value("${spring.application.name}")    private String appName;    @Autowired    public DemoServiceImpl(ClientDemoMapper demoMapper, DDemoClient dDemoClient, EDemoClient eDemoClient) {        this.demoMapper = demoMapper;        this.dDemoClient = dDemoClient;        this.eDemoClient = eDemoClient;    }    @Override    @Transactional    @TxcTransaction    public String execute(String value) {        String dResp = dDemoClient.rpc(value);        String eResp = eDemoClient.rpc(value);        Demo demo = new Demo();        demo.setDemoField(value);        demo.setAppName(appName);        demo.setCreateTime(new Date());        demo.setGroupId(DTXLocalContext.getOrNew().getGroupId());        demo.setUnitId(DTXLocalContext.getOrNew().getUnitId());        demoMapper.save(demo);        return dResp + " > " + eResp + " > " + "ok-client";    }}