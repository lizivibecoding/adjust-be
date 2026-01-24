package com.hongguoyan.module.pay.convert.wallet;

import com.hongguoyan.framework.common.pojo.PageResult;
import com.hongguoyan.module.pay.controller.admin.wallet.vo.transaction.PayWalletTransactionRespVO;
import com.hongguoyan.module.pay.controller.app.wallet.vo.transaction.AppPayWalletTransactionRespVO;
import com.hongguoyan.module.pay.dal.dataobject.wallet.PayWalletTransactionDO;
import com.hongguoyan.module.pay.service.wallet.bo.WalletTransactionCreateReqBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayWalletTransactionConvert {

    PayWalletTransactionConvert INSTANCE = Mappers.getMapper(PayWalletTransactionConvert.class);

    PageResult<PayWalletTransactionRespVO> convertPage2(PageResult<PayWalletTransactionDO> page);

    PayWalletTransactionDO convert(WalletTransactionCreateReqBO bean);

}
