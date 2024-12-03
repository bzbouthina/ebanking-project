import dayjs from 'dayjs';
import { PaymentType } from 'app/shared/model/enumerations/payment-type.model';
import { PaymentStatus } from 'app/shared/model/enumerations/payment-status.model';

export interface IPayment {
  id?: number;
  transactionId?: string;
  login?: string;
  amount?: number;
  paymentType?: keyof typeof PaymentType;
  paymentDate?: dayjs.Dayjs;
  description?: string | null;
  status?: keyof typeof PaymentStatus;
  createdBy?: string;
  createdDate?: dayjs.Dayjs;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  paymentId?: string;
}

export const defaultValue: Readonly<IPayment> = {};
