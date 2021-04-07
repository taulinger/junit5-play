package de.taulinger.junitplay;

import com.microsoft.playwright.options.LoadState;
import de.taulinger.junitplay.TestUtils.ReplaceCamelCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@DisplayNameGeneration(ReplaceCamelCase.class)
class MainMenuTest extends TestUtils {

    @Nested
    class GivenAMainMenuForDesktopUsage {

        @Nested
        class WhenPageLoaded {

            @ParameterizedTest
            @EnumSource(Browser.class)
            void thenTheMainMenuShouldShowAButtonIn(Browser type) {
                setup(type, ClientType.Desktop);
                
                page.waitForLoadState(LoadState.NETWORKIDLE);
                
                assertEquals("Menü öffnen", page.innerText(".ga-visible-collapsed"));
            }
        }

    }

    @Nested
    class GivenAMainMenuForMobileUsage {

        @Nested
        class WhenPageLoaded {

            @ParameterizedTest
            @EnumSource(Browser.class)
            void thenTheMainMenuShouldShowAButtonIn(Browser type) {

                setup(type, ClientType.Mobile);
               
                page.waitForLoadState(LoadState.NETWORKIDLE);
                
                assertEquals("Menü", page.innerText(".btn.ga-menu-bt.ng-scope"));
            }
        }
    }
}
