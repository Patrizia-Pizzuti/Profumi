import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfumoDettaglioComponent } from './profumo-dettaglio.component';

describe('ProfumoDettaglioComponent', () => {
  let component: ProfumoDettaglioComponent;
  let fixture: ComponentFixture<ProfumoDettaglioComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProfumoDettaglioComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfumoDettaglioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
