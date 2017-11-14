import { Component, Input, Output, EventEmitter } from '@angular/core';

import { Ontoimage } from './ontoimage.model';

@Component({
    selector: 'imageontologia',
    templateUrl: 'ontoimage.componet.html'
})
export class OntoimageComponent {

    public showFullImage:boolean=false;


    @Input()
    private ontoimage: Ontoimage;
    
    full(state:boolean){
        this.showFullImage=state;
    }
}