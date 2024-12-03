import dayjs from 'dayjs';
import { IPayment } from 'app/shared/model/payment/payment.model';

export interface IBillPayment {
  id?: number;
  billReference?: string;
  billIssuer?: string;
  amount?: number;
  paymentDate?: dayjs.Dayjs;
  description?: string | null;
  payment?: IPayment;
}

export const defaultValue: Readonly<IBillPayment> = {};
