import { Component, Input, Output, EventEmitter } from '@angular/core';

import { ItemData } from './itemdata.model';
import { Ontologia } from './ontologia.model';

@Component({
    selector: 'itemdata',
    templateUrl: './itemdata.component.html'
})
export class ItemdataComponent {

    @Input()
    private itemdata: ItemData;
    @Input()
    private resourceOnto: Ontologia;
    @Input()
    private titleSource: string;
    
    @Output() 
    inputDataChange=new EventEmitter();
    
    setHide(){
        console.log("sss");
    }
}