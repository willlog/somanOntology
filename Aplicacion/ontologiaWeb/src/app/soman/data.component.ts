import { Component, Input, Output, EventEmitter } from '@angular/core';

import { dataComponentModel } from './data.model';
import { readModel } from './read.model';

@Component({
    selector: 'dataontologia',
    templateUrl: 'data.component.html'
})
export class DataComponent {
    private showItems:boolean= false;
    @Input()
    private dataontologia: dataComponentModel;
    
    show(){
        this.showItems=true;
    }
    hide(){
        this.showItems=false;
    }
}