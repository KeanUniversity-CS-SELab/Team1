import {Component, NgModule} from '@angular/core';
import {HttpParams, HttpClient, HttpResponse} from '@angular/common/http';
import {DateRangeData, DayData, CompanyInfoData} from '../assets/Models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {

  title = 'Team 1 IEF Stocks';
  fdID = null; infoID = null;
  dataFD = [null, null, null]; fromdate = '2018-10-01'; todate = '2019-10-01';
  dataInfo = [null, null, null];
  data30: DayData[]; date30 = null;
  symbols = [{id: 1, name: 'RFEM'}, {id: 2, name: 'GOOGL'}, {id: 3, name: 'UNH'}];

  constructor(private http: HttpClient) {}

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
    const p = new HttpParams()
      .set('date', this.date30);
    const options = {params: p};
    this.http.get('http://localhost:8080/IEX30', options)
      .subscribe((data: DayData[]) => {
        this.data30 = data;
        console.log(data);
      });
  }
}
