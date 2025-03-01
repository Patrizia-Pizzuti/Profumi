import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfumiComponent } from './profumi.component';

describe('ProfumiComponent', () => {
  let component: ProfumiComponent;
  let fixture: ComponentFixture<ProfumiComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProfumiComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfumiComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
