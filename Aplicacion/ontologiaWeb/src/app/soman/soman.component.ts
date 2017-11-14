import { Component, OnInit, HostBinding,Injectable,ViewChild,ViewEncapsulation} from '@angular/core';
import { Http } from '@angular/http';
import {Router, ActivatedRoute, ParamMap,NavigationStart} from '@angular/router';
import 'rxjs/add/operator/switchMap';


import { Ontologia } from './ontologia.model';
import { OntologiaComponent } from './ontologia.component';
import { OntologiaService } from './ontologia.service';

//ontologia for fields headers

import { isDescribedBy } from './isDescribedBy.model';
import { isRelatedBy } from './isRelatedBy.model';


import { parameter } from './parameter.model';

//set Data on Ontologia Model for component

import { dataComponentModel } from './data.model';
import { linkModel } from './link.model';
import { Ontoimage } from './ontoimage.model';
import { readModel } from './read.model';
import { emailModel } from './email.model';
import { dataModel } from './dataModel.model';
import { MapaInformacion } from './mapa.model';
import {Observable} from 'rxjs/Rx';

import { process, State } from '@progress/kendo-data-query';
import {
    PageChangeEvent,
    GridComponent,
    GridDataResult,
    DataStateChangeEvent,
} from '@progress/kendo-angular-grid';

//module for filter
import { filterBy, FilterDescriptor, CompositeFilterDescriptor } from '@progress/kendo-data-query';

const flatten = filter => {
    const filters = filter.filters;
    if (filters) {
        return filters.reduce((acc, curr) => acc.concat(curr.filters ? flatten(curr) : [curr]), []);
    }
    return [];
};

@Component({
  selector: 'soman-app',
  encapsulation: ViewEncapsulation.None,
  templateUrl: './soman.component.html',
})

@Injectable()
export class SomanComponent implements OnInit  { 
    
    private resource: Ontologia={img:[],
        data:[],
        read: null,
        link: [],
        relatedby:null,
        email: []};

    private titleSource: string="";
    private resourceOnto: Ontologia;
    public checked: boolean = false;
    private puntoComparar: MapaInformacion[] = [];
    private existe: boolean = false;
    public  showmap: boolean = false;
    private showheader: boolean = false;


   
    private search: string;

    private ontologias: Ontologia[] = [];

    private isDescribedBys: isDescribedBy[]=[];
    private isDescribedBysColumn: isDescribedBy[]=[];
    
    
    private isDescribedBysDataDescribeField: isDescribedBy;    

    private dataModels:dataModel[]=[];

    private isRelatedBys: isRelatedBy[]=[];

    private Parameters: parameter[]=[];
    

    //para kendo grid

    public ontologiaskendo: any[] = [];
    public nameresourcegrid:string="";
    public filter: CompositeFilterDescriptor;
    
    //for map data

    public latitud:any = null;
    public longitud:any = null ;

    public ontoDataLink1 :any[];
    public dataItem1:any;
    public ontologias1:any[];
    public nameProyecto:any = null;
    public perfilProyecto:any = null;
    public correo:any=null;

    //for paging

    private pageSize: number = 10;
    private skip: number = 0;
    private gridData: GridDataResult;
    private items: any[];
    private data: Object[];
   
    private state: State = {
        skip: 0,
        take: 10,
      };
    
      private stateclear: State = {
        skip: 0,
        take: 10,
      };
    
    public menuItems:any[]=[];

    //for button +-

    public butonPanelField:boolean=true;
    public butonPanelMap:boolean=false;
    public hMap:number=300;
    public butonPanelHead:boolean=false;
    
    
    // for mail

    public opened: boolean = false;
    public to:string="";
    public from:string="";
    public sms:string="";
    public smsFill:boolean=false;
    public sendEmail:boolean=false;


    constructor(private route: ActivatedRoute,private router: Router,private http: Http, private ontologiasService: OntologiaService) { 
        
        router.events.subscribe(event => {
            if(event instanceof NavigationStart) {
                /*this.ontologias=[];
                this.gridData=null;
                this.resource={img:[],
                    data:[],
                    read: null,
                    link: [],
                    relatedby:null,
                    email: []};
                this.showheader=false;
                this.refresh(this.route.snapshot.paramMap); */
            }
           
          });
    }

    ngOnInit() {
        this.refresh(this.route.snapshot.paramMap); 
        this.getOntologyMenu();
        
    }
    

    


    private refresh(resource: any) {
        try{
            this.ontologias=[];
            let name:string="";
            this.ontologiasService.getOntologias(resource).subscribe(
                    ontologias => {
                        
                        for(let nameonto in ontologias){
                            name=nameonto;
                            this.nameresourcegrid=ontologias[name]['title'].name;
                        }
                        
                        this.isDescribedBys=[];
                        this.isDescribedBysColumn=[];
                        
                        for (let key in ontologias[name]['isDescribedBy']) {
                            var id=key;
                            let text="";
                            let tipo="";
                            let kendo="string";
                            let visible=true;
                        
                            text=ontologias[name]['isDescribedBy'][key]['title'];
                            tipo=ontologias[name]['isDescribedBy'][key]['type'];
                            if(ontologias[name]['isDescribedBy'][key]['typekendo']){
                                kendo=ontologias[name]['isDescribedBy'][key]['typekendo'];
                            }   

                            if(ontologias[name]['isDescribedBy'][key]['visible']){
                                visible=(ontologias[name]['isDescribedBy'][key]['visible'].toLowerCase() === 'true');
                            }
                            let colLL=true;

                            if (tipo=='LongitudeDataItem') {
                                colLL=false;
                            }
                            if (tipo == 'LatitudeDataItem') {
                                colLL=false;
                            }
                            if(colLL){
                                this.isDescribedBysColumn.push({Id:id.charAt(0).toUpperCase() + id.slice(1),Text:text,Tipo:tipo,Kendo:kendo,Visible:visible});                        
                            }
                            this.isDescribedBys.push({Id:id.charAt(0).toUpperCase() + id.slice(1),Text:text,Tipo:tipo,Kendo:kendo,Visible:visible});                        
                        }
                        
                        ontologias[name]['isDescribedBy']=this.isDescribedBys;
                        

                        //relaciones
                        
                        this.isRelatedBys=[];
                        
                        
                    
                        for (let key in ontologias[name]['isRelatedBy']) {
                            let valueIsRealtedBy=ontologias[name]['isRelatedBy'][key]
                            
                            this.Parameters=[];
                            for (let keyParameter in valueIsRealtedBy) {
                                if (typeof valueIsRealtedBy[keyParameter]['parameter'] == "undefined"){
                                    break;
                                }
                                
                                this.Parameters.push({Name:valueIsRealtedBy[keyParameter]['parameter'],Value:"0"});
                                
                            }
                            
                        
                            if(this.Parameters.length>0){
                                this.isRelatedBys.push({Value:key,Params:this.Parameters}); 
                            }else{
                                
                                this.isRelatedBys.push({Value:key}); 
                            } 
                        }
                    
                        
                        ontologias[name]['isRelatedBy']=this.isRelatedBys;
                        
                        this.ontologias = ontologias[name];
                        
                        let dataFieldItem:any[]=[];
                        
                        for(let keyIsDescibe in this.ontologias['isDescribedBy']){
                            if(this.isDescribedBys[keyIsDescibe].Tipo=="DateDataItem"){
                                dataFieldItem.push({field:this.ontologias['isDescribedBy'][keyIsDescibe].Id});
                            }  
                        }
                    
                        if(this.ontologias['mappedTo']){
                            if(dataFieldItem){
                                for(let keyParameter in this.ontologias['mappedTo'].Data){
                                    for(let keyItemData in this.ontologias['mappedTo'].Data[keyParameter]){
                                        for(let keyItemDateField in dataFieldItem){
                                            if(keyItemData==dataFieldItem[keyItemDateField].field)
                                                this.ontologias['mappedTo'].Data[keyParameter][keyItemData]=this.parseDate(this.ontologias['mappedTo'].Data[keyParameter][keyItemData]);
                                        } 
                                    }        
                                }
                            }

                            
                            this.gridData=process(this.ontologias['mappedTo'].Data, this.state);
                        }
                        
                    }
                    
                    
            
                    
            );
        }catch(e){
            
        }
        
       
    }
        
    verdata(d:any){
       console.log(d);
    }
    
    headerComponent(data:Ontologia,discribeBy:any,relatedByItems:any,typeRefres:boolean){
        
        if(typeRefres){
            
            

            this.isDescribedBysDataDescribeField=discribeBy;
            this.showmap=false;
            this.puntoComparar=[];
            
            let datas:dataComponentModel[]=[];
            let links:linkModel[]=[];
            let images:Ontoimage[]=[];
            let reads:readModel=null;
            let emails:emailModel[]=[];
            let relatedbys:dataModel=data['Relacionado'];
        
            for(let onto in data){
            
                for(let keyIsDescibe in discribeBy){
                    try{
                        if((this.isDescribedBys[keyIsDescibe]['Id']).toLowerCase()==onto.toLowerCase()){
                            if(this.isDescribedBys[keyIsDescibe].Tipo=="StringDataItem"){
                                datas.push({Id:onto,Value:data[onto]});
                            }else if(this.isDescribedBys[keyIsDescibe].Tipo=="LinkDataItem"){
                                links.push({Id:onto,Src:data[onto],Value:data[onto]});
                            }else if(this.isDescribedBys[keyIsDescibe].Tipo=="ImageDataItem"){
                                images.push({Id:onto,Src:data[onto],alt:data[onto]});
                            }else if(this.isDescribedBys[keyIsDescibe].Tipo=="ReadDataItem"){
                                reads=JSON.parse(data[onto]);
                            }else if(this.isDescribedBys[keyIsDescibe].Tipo=="EmailDataItem"){
                                emails.push(data[onto]);
                            }

                            break;
                        }
                    }catch(e){}
                }
            }

            relatedbys=relatedByItems;
            
            
            this.resource={img:images,
                data:datas,
                read: reads,
                link: links,
                relatedby:relatedbys,
                email: emails};
                
            //console.log(this.resource);
            let elm = document.getElementById('collapseHead');
            if(elm)
                elm.className='collapse show';
            this.butonPanelHead=false;
            return this.resource;
        }
    }

    onSelect(   resource: isRelatedBy,
                data: any,
                source: any,
                isDescribeByItems:any,
                isRelatedByFullItems:any,
                refres:boolean) {
        
        
        this.showheader=true;
        this.showmap=false;
        this.puntoComparar=[];
       
        this.headerComponent(data,isDescribeByItems,isRelatedByFullItems,refres);
        this.titleSource=source;
        this.resourceOnto=data;
        
        let dataLink:string = "";
        let linresource:string = "";
        let jsonObject1 : any = ""
        
        linresource=resource['Value'];
        dataLink="\"source\":\""+source+"\",";
        if(typeof resource['Params'] != "undefined"){
            for(let getParameterValues in resource['Params']){
                console.log(resource['Params'][getParameterValues].Name);
                for(let keyData in data){
                        console.log(keyData);
                        try{                      
                            if((keyData).toLowerCase()==(resource['Params'][getParameterValues].Name).toLowerCase()){
                                dataLink=dataLink+'\"'+resource['Params'][getParameterValues].Name+'\":\"'+data[keyData]+'\",';
                                break;
                            }
                        }catch(e){

                        }
                    
                }
                
            }
        }
    
        jsonObject1=JSON.parse("{\"params\":{"+"\"resource\":\""+linresource+"\","+dataLink.substring(0,dataLink.length-1)+"}}");
        let paramurl:ParamMap=jsonObject1;
        this. clearfilter();
        this.ontologias=[];
        this.refresh(paramurl);   
    }

    menuResource(linkMenu:string){
        let jsonObject1 : any = ""
        jsonObject1=JSON.parse("{\"params\":{"+"\"resource\":\""+linkMenu+"\"}}");
        let paramurl:ParamMap=jsonObject1;
        this. clearfilter();
        this.ontologias=[];
        this.puntoComparar=[];
        this.gridData.data=[];
        this.gridData.total=0;
        this.showheader=false;
        this.showmap=false;
        this.hiddenColumns = [];
        this.refresh(paramurl);   
    }


    
    graficar(datoLatLonNam:any, tipoDato:any, ontodatalink:any, dataitem:any, ontolo:any )
    {        

        this.showMap();
        
        if(ontodatalink != ''){
        this.ontoDataLink1=ontodatalink;
        }

        if(dataitem != ''){
        this.dataItem1=dataitem;
        }

        if(ontolo != ''){
            this.ontologias1=ontolo;
        }
  
        if(tipoDato == 'profileUrl'){
            this.perfilProyecto = datoLatLonNam;
        }

        if(tipoDato == 'latitude'){
            this.latitud = datoLatLonNam;
        }
     
        if(tipoDato=='longitude'){
            this.longitud=datoLatLonNam;
        }

        if(this.nameProyecto==null){
            if(tipoDato == 'title'){
                this.nameProyecto=datoLatLonNam;
              }
            }
    
            if(this.nameProyecto==null){
                if(tipoDato == 'firstname'){
                    this.nameProyecto=datoLatLonNam;
                  }
                }
    
        if( this.longitud!=null && this.latitud!=null){

            if(tipoDato == 'name'){
            this.nameProyecto=datoLatLonNam;
            }

            if(tipoDato == 'email'){
                this.correo= datoLatLonNam;
                this.existe=false;
                this.sacarDatos({       name: this.nameProyecto, 
                                        latitud: this.latitud,
                                        longitud: this.longitud,
                                        profileUrl: this.perfilProyecto,
                                        co: this.correo,
                                        ontoDataLink : this.ontoDataLink1,
                                        dataItem: this.dataItem1,
                                        ontologias: this.ontologias1
                                    });

            if(this.existe==false){

                        
                
                this.puntoComparar.push({
                                        name: this.nameProyecto, 
                                        latitud: this.latitud,
                                        longitud: this.longitud,
                                        profileUrl: this.perfilProyecto,
                                        co: this.correo,
                                        ontoDataLink : this.ontoDataLink1,
                                        dataItem: this.dataItem1,
                                        ontologias: this.ontologias1
                                    });
                let elm = document.getElementById('collapseMap');
                if(elm)
                    elm.className='card-block collapse show';
                this.butonPanelMap=false;
                this.hMap=300;
                
            }

             this.latitud=null;
             this.longitud=null;
             this.nameProyecto=null;
             this.correo=null;
             this.perfilProyecto=null;
         
        }
 
    }


        
         
            
    }



    sacarDatos(cadena:MapaInformacion){

      for(let contador in this.puntoComparar){ 
          if(cadena.name===this.puntoComparar[contador].name && cadena.latitud===this.puntoComparar[contador].latitud && 
             cadena.longitud===this.puntoComparar[contador].longitud){
             this.existe = true;
             break;
          }     
         

      }
      
    }
   
    
    
    
    showMap(){
        if (this.puntoComparar.length>0)
            {
                this.showmap=true;
            
            }else{
                this.showmap=false;
            }
        
    }

    public hiddenColumns: string[] = [];
    
    public isHidden(columnName: string): boolean {
        return this.hiddenColumns.indexOf(columnName) > -1;
    }
    
    public isDisabled(columnName: string): boolean {
        //return this.columns.length - this.hiddenColumns.length === 1 && !this.isHidden(columnName);
        return false;
    }
    
    public hideColumn(columnName: string): void {
        const hiddenColumns = this.hiddenColumns;
        if (!this.isHidden(columnName)) {
          hiddenColumns.push(columnName);
        } else {
          hiddenColumns.splice(hiddenColumns.indexOf(columnName), 1);
        }
    }

    public restoreColumns(): void {
        let checkedAllItems: boolean = false;
        if(this.checked){
            checkedAllItems=true;
        }
        this.hiddenColumns=[];
        for (let key in this.isDescribedBysColumn) 
        {
            this.isDescribedBysColumn[key].Visible=checkedAllItems;
        }
    }

    /*protected pageChange(event: PageChangeEvent): void {
        this.skip = event.skip;
        this.gridView=null;
        this.gridView = {
            data:this.gridData.slice(this.skip, this.skip + this.pageSize),
            total: this.gridData.length
        };
    }*/
    
    protected dataStateChange(state: DataStateChangeEvent): void {
        this.showmap=false;
        this.state = state;
        if(this.ontologias['mappedTo'])
        {
            this.gridData.data=[];
            this.gridData.total=0;
            this.gridData = process(this.ontologias['mappedTo'].Data, this.state);
        }
            
    }

    public clearfilter(){
        this.state = this.stateclear;
        if(this.ontologias['mappedTo']){
            this.gridData.data=[];
            this.gridData.total=0;
            this.gridData = process(this.ontologias['mappedTo'].Data, this.stateclear);
        }
            
    }

    public parseDate(value: string): Date {
        let reggie = /(\d{2})-(\d{2})-(\d{4})/;
        let dateArray = reggie.exec(value);
        let dateObject = new Date(
            (+dateArray[3]),
            ((+dateArray[2])) - 1, // Careful, month starts at 0!
            (+dateArray[1])
        );
        return dateObject;
    }

    public dataImageItem(url:string):Ontoimage{
        let imagesItem:Ontoimage={Id:url,Src:url,alt:url};
        return imagesItem;
    }
    public dataEmailItem(email:string):emailModel{
        let emailItem:emailModel={mail:email};
        return emailItem;
    }

    public getOntologyMenu(){
        this.menuItems=[];
        this.showheader=false;
        this.showmap=false;
        this.titleSource="";
        this.ontologiasService.getOntologyMenu().subscribe(
            menus => {
                this.menuItems=menus.Menu;        
            }    
        );
    }

    //for panel button +-

    changeButtonPanelItem(){
        if(this.butonPanelField){
            this.butonPanelField=false;
        }else{
            this.butonPanelField=true;
        }
    }

    changeButtonPanelMap(){
        if(this.butonPanelMap){
            this.butonPanelMap=false;
            this.hMap=300;
        }else{
            this.butonPanelMap=true;
            this.hMap=63.5;
        }
    }

    
    
    changeButtonPanelHead(){
        if(this.butonPanelHead){
            this.butonPanelHead=false;
        }else{
            this.butonPanelHead=true;
        }
    }


    

    emailValidator(email:string): boolean {
        var EMAIL_REGEXP = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

        if (!EMAIL_REGEXP.test(email)) {
            return false;
        }
        return true; 
    }

    close(status:boolean) {
        if(status){
            if(this.from.trim().length>0){
                if(this.emailValidator(this.from)){                       
                    if(this.sms.trim().length>0){
                        this.sendEmail=true;
                        let timer = Observable.timer(4000);
                        timer.subscribe(t=>{
                            this.opened = false;
                            this.sendEmail=false;
                            this.from="";
                            this.sms="";
                        });
                    }else{
                        this.smsFill=true;
                    } 
                }  
            }else{
                this.from="@";
            }
        }else
        {
            this.opened = false;
            this.sendEmail=false;
            this.from="";
            this.sms="";
        }
            
    }

    OpenDialogMail(event):void {
        console.log(event);
        this.opened = event.open;
        this.to=event.to;
    }
    

    
}