import { Component,OnInit,Input } from '@angular/core';
import { MapaInformacion } from './mapa.model';

import { SomanComponent } from './soman.component';

@Component({
    selector:'mapa',
    templateUrl: './mapa.component.html',
    styleUrls: ['mapa.component.css']
})


export class MapaComponent implements OnInit{
    lat: number;
    lng: number;

    @Input() zoom: number;
    @Input() puntos: MapaInformacion;
    ngOnInit() {
    
    }
    
    constructor(public so: SomanComponent) { }

    public centrarPunto(punto:any){
        try {
            if(punto.length==1){
                for(let keyIsDescibe in punto){
                    this.lat=punto[keyIsDescibe].latitud;
                    this.lng=punto[keyIsDescibe].longitud;
                }
            }else{
                this.lat=-1.2572469;
                this.lng=-78.6434092;
            }
            
        } catch (error) {
            
        }
        
    }
}