import {Component, NgModule, enableProdMode} from '@angular/core';
import {DatePipe} from '@angular/common';
import {HttpParams, HttpClient, HttpResponse, HttpHeaders} from '@angular/common/http';
import {DateRangeData, DayData, CompanyInfoData} from '../assets/Models';
import {GoogleChartsModule} from 'angular-google-charts';
enableProdMode();

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [DatePipe]
})

export class AppComponent {
  fdID = null; infoID = null;
  companyID = null; dataChart = [];
  dataFD = [null, null, null]; fromdate = '2019-11-01'; todate = '2019-12-07';
  dataInfo = [null, null, null];
  data30: DayData[] = [];
  date30 = this.datePipe.transform(new Date(),'yyyy-MM-dd');
  symbols = [{id: 1, name: 'RFEM'}, {id: 2, name: 'GOOGL'}, {id: 3, name: 'UNH'}];

  title = 'Close Values';
  type = 'LineChart';
  columnNames = ["Date", "RFEM", "GOOGL", "UNH"];
  options = {
    hAxis: {
     title: 'Date'
    },
    vAxis:{
     title: '$'
    }
  };
  width = 900;
  height = 600;

  constructor(private http: HttpClient, private datePipe: DatePipe) {}

  getCompanyInfo(company): void{
    this.infoID = company;
    const p = new HttpParams()
      .set('company', company);
    const options ={params: p}
    this.http.get('http://localhost:8080/companyInfo',options)
      .subscribe((data: CompanyInfoData) => {
        this.dataInfo[company]=data;
      });
  }
  getDateFilter(company): void{
    this.fdID = company;
    const p = new HttpParams()
      .set('symbolid', company)
      .set('from', this.fromdate)
      .set('to', this.todate);
    const options = {params: p};
    this.http.get('http://localhost:8080/daterange', options)
      .subscribe((data: DateRangeData) => {
        this.dataFD[company] = data;
      });
  }
  getThirtyDay(): void{
    var p = new HttpParams()
      .set('date', this.date30);
    var options = {
      params: p
    };
    this.http.request('post','http://localhost:8080/IEX30', options)
      .subscribe((data: DayData[]) => {
        this.data30 = data;
        if(data){
          this.dataChart = [];
          for(var i = 0; i < this.data30.length;){
            var temp =[this.data30[i].date, 0, 0, 0];
            while(this.data30[i]!=undefined && this.data30[i].date==temp[0]){
              temp[parseInt(this.data30[i].companyID)]=parseInt(this.data30[i].close);
              i++;
            }
            console.log(temp);
            this.dataChart.push(temp);
          }
        }
      }
    );
  }
}
