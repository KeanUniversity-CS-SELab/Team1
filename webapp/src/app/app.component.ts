import {Component, NgModule} from '@angular/core';
import {HttpParams, HttpClient, HttpResponse} from '@angular/common/http';
import {DateRangeData, DayData} from '../assets/Models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {

  title = 'Team 1 IEF Stocks';
  dataFD: DateRangeData; fromdate = null; todate = null;
  dataInfo = null;
  data30: DayData[]; date30 = null;
  constructor(private http: HttpClient) {}

  getCompanyInfo(company): void{
    const p = new HttpParams()
      .set('symbolid', company)
    this.http.get('http://localhost:8080/tbd')
      .subscribe((data: any[]) => {
        console.log(data);
        this.dataInfo = data;
      });
  }
  getDateFilter(company): void{
    console.log(this.fromdate, this.todate);
    const p = new HttpParams()
      .set('symbolid', company)
      .set('from', this.fromdate)
      .set('to', this.todate);
    const options = {params: p};
    this.http.get('http://localhost:8080/daterange', options)
      .subscribe((data: DateRangeData) => {
        this.dataFD = data;
        console.log(data);
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
