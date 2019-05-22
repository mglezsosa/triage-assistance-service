package tech.sosa.triage_assistance_service.domain.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClinicalFindingIdShould {

    @Test
    public void represent_itself_correctly_as_string() {
        String strExp1 = "397956004 |prosthetic arthroplasty of the hip|:"
                + "363704007 |procedure site| = (24136001 |hip joint structure|: "
                + "272741003 |laterality| = 7771000 |left|),"
                + "{ 363699004 |direct device| = 304120007 |total hip replacement prosthesis|,"
                + "260686004 |method| = 257867005 |insertion - action|}";

        String strExp2 =
                "71388002 |Procedure| :" + "{  260686004 |Method| = 129304002 |Excision - action| ,"
                        + "   405813007 |Procedure site - direct| = 15497006 |Ovarian structure| },"
                        + "{  260686004 |Method| = 129304002 |Excision - action| ,"
                        + "   405813007 |Procedure site - direct| = 31435000 |Fallopian tube structure| }";

        ClinicalFindingId clinicalFindingId1 = new ClinicalFindingId(strExp1);
        ClinicalFindingId clinicalFindingId2 = new ClinicalFindingId(strExp2);

        assertEquals("397956004:363704007=(24136001:272741003=7771000),"
                        + "{363699004=304120007,260686004=257867005}",
                clinicalFindingId1.toString());

        assertEquals("71388002:{260686004=129304002,405813007=15497006},"
                        + "{260686004=129304002,405813007=31435000}",
                clinicalFindingId2.toString());
    }

}
