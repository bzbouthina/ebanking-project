import dayjs from 'dayjs';
import { IBankAccount } from 'app/shared/model/accounts/bank-account.model';

export interface ILocaltransfer {
  id?: number;
  senderAccountNumber?: string;
  recipientAccountNumber?: string;
  recipientBankName?: string;
  recipientBankBranch?: string | null;
  amount?: number;
  transactionDate?: dayjs.Dayjs;
  description?: string | null;
  bankAccount?: IBankAccount;
}

export const defaultValue: Readonly<ILocaltransfer> = {};
