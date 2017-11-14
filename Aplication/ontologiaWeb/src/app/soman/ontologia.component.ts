import { Component,OnInit ,Input, Output, EventEmitter } from '@angular/core';
import { Http } from '@angular/http';
import { OntologiaService } from './ontologia.service';

@Component({
    selector: 'ontologia',
    templateUrl: './ontologia.component.html'
})
export class OntologiaComponent {
    public menuItems:any[]=[];

    constructor(private ontologiasService: OntologiaService) { 
    }
    ngOnInit() {
        this.getOntologyMenu();
        
    }
    public getOntologyMenu(){
        this.menuItems=[];
        
        this.ontologiasService.getOntologyMenu().subscribe(
            menus => {
                this.menuItems=menus.Menu;        
            }    
        );
    }

}