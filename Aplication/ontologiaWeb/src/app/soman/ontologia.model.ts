import { Ontoimage } from './ontoimage.model';
import { readModel } from './read.model';
import { linkModel } from './link.model';
import { dataModel } from './dataModel.model';
import { dataComponentModel } from './data.model';
import { emailModel } from './email.model';


export interface Ontologia {
    img?:Ontoimage[];
    data?:dataComponentModel[];
    read?: readModel;
    link?: linkModel[];
    relatedby?: dataModel;
    email?: emailModel[];
}