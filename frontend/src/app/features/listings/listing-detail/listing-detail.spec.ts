import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListingDetailComponent } from './listing-detail';

describe('ListingDetail', () => {
  let component: ListingDetailComponent;
  let fixture: ComponentFixture<ListingDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListingDetailComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ListingDetailComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
