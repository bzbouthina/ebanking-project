import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment/payment.model';

export interface ICardPayment {
  id?: number;
  cardNumber?: string;
  cardExpiryDate?: dayjs.Dayjs;
  cardHolderName?: string;
  cvv?: string;
  amount?: number;
  paymentDate?: dayjs.Dayjs;
  description?: string | null;
  payment?: IPayment;
}

export const defaultValue: Readonly<ICardPayment> = {};
