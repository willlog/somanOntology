import { isDescribedBy } from './isDescribedBy.model';
import { isRelatedBy } from './isRelatedBy.model';

export interface dataModel{
    Id: string;
    Value?: string;
    IsDescribe?: isDescribedBy;
    IsRelatedBy?: isRelatedBy[];
}