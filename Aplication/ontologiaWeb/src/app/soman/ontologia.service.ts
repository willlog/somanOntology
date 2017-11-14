import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Globals} from './global';
import 'rxjs/Rx';

import { Ontologia } from './ontologia.model';


import { GridDataResult } from '@progress/kendo-angular-grid';
import { toODataString } from '@progress/kendo-data-query';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import 'rxjs/add/operator/map';

@Injectable()
export class OntologiaService {
    
    constructor(private http: Http) { }

    getOntologias(name: any) {
      let urlresource:string="";
        

        if(name['keys']){
            for(let s in name['keys']){
                if(name['keys'][s]=="resource"){
                    urlresource=name.get(name['keys'][s]);
                }else{
                    urlresource=urlresource+'&'+name['keys'][s]+'='+name.get(name['keys'][s]);
                }
            }
        }else{
            for(let sera in name){
                for(let itemParam in name[sera]){
                    if(itemParam=="resource"){
                        urlresource=name[sera][itemParam];
                    }else{
                        urlresource=urlresource+'&'+itemParam+'='+name[sera][itemParam];
                    }
                   
                }
             }
        }
        
        console.log(urlresource);
        return this.http.get(Globals.BASE_URL_API_REST+urlresource)
            .map(response => response.json())
            .catch(error => this.handleError(error));
    }

    getOntologiasAnte(name: any) {
        let urlresource:string="";
      
          for(let s in name['keys']){
              if(name['keys'][s]=="resource"){
                  urlresource=name.get(name['keys'][s]);
              }else{
                  urlresource=urlresource+'&'+name['keys'][s]+'='+name.get(name['keys'][s]);
            }
              
        }
         // console.log(urlresource);
        return this.http.get(Globals.BASE_URL_API_REST+urlresource)
              .map(response => response.json())
              .catch(error => this.handleError(error));
    }
  
    getOntologyMenu() {
         // console.log(urlresource);
        return this.http.get(Globals.BASE_URL_API_REST_MENU)
              .map(response => response.json())
              .catch(error => this.handleError(error));
    }

    private handleError(error: any) {
        console.error(error);
        return Observable.throw("Server error (" + error.status + "): " + error.text())
    }
}