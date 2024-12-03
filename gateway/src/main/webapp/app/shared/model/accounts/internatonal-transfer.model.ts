import dayjs from 'dayjs';
import { IBankAccount } from 'app/shared/model/accounts/bank-account.model';
import { CurrencyType } from 'app/shared/model/enumerations/currency-type.model';

export interface IInternatonalTransfer {
  id?: number;
  senderAccountNumber?: string;
  recipientIban?: string;
  swiftCode?: string;
  recipientName?: string;
  amount?: number;
  currency?: keyof typeof CurrencyType;
  transactionDate?: dayjs.Dayjs;
  description?: string | null;
  bankAccount?: IBankAccount;
}

export const defaultValue: Readonly<IInternatonalTransfer> = {};
