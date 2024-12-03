import customer from 'app/entities/customers/customer/customer.reducer';
import document from 'app/entities/customers/document/document.reducer';
import bankAccount from 'app/entities/accounts/bank-account/bank-account.reducer';
import localtransfer from 'app/entities/accounts/localtransfer/localtransfer.reducer';
import internatonalTransfer from 'app/entities/accounts/internatonal-transfer/internatonal-transfer.reducer';
import payment from 'app/entities/payment/payment/payment.reducer';
import qRPayment from 'app/entities/payment/qr-payment/qr-payment.reducer';
import billPayment from 'app/entities/payment/bill-payment/bill-payment.reducer';
import cardPayment from 'app/entities/payment/card-payment/card-payment.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  customer,
  document,
  bankAccount,
  localtransfer,
  internatonalTransfer,
  payment,
  qRPayment,
  billPayment,
  cardPayment,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
