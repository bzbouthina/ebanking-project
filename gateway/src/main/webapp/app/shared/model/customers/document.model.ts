import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/customers/customer.model';
import { DocumentType } from 'app/shared/model/enumerations/document-type.model';

export interface IDocument {
  id?: number;
  documentNumber?: string;
  documentType?: keyof typeof DocumentType;
  issueDate?: dayjs.Dayjs | null;
  expiryDate?: dayjs.Dayjs | null;
  filePath?: string;
  uploadedAt?: dayjs.Dayjs;
  customer?: ICustomer | null;
}

export const defaultValue: Readonly<IDocument> = {};
