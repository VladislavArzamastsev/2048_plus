package command.settings;

import dao.preferences.PreferencesDao;
import handler.CommandHandler;
import mock.ThisThreadCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import preferences.UserPreferences;
import testUtils.ThemeUtils;
import testUtils.UserPreferencesUtils;
import view.theme.Theme;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ThemeChangeCommandTest {

    private CommandHandler commandHandler;
    private UserPreferences userPreferences;
    private PreferencesDao preferencesDAO;
    private ThemeChangeCommand themeChangeCommand;

    @BeforeEach
    void init() {
        userPreferences = spy(UserPreferencesUtils.getDefaultUserPreferences());
        commandHandler = spy(new ThisThreadCommandHandler());
        preferencesDAO = mock(PreferencesDao.class);
        themeChangeCommand = new ThemeChangeCommand(userPreferences, preferencesDAO, commandHandler);
    }

    @Test
    void testNullAsParam() {
        assertThrows(NullPointerException.class, () -> themeChangeCommand.setParam(null));
    }

    @Test
    void nullConstructorArgTest() {
        assertThrows(NullPointerException.class,
                () -> new ThemeChangeCommand(userPreferences, preferencesDAO, null));
        assertThrows(NullPointerException.class,
                () -> new ThemeChangeCommand(null, preferencesDAO, commandHandler));
        assertThrows(NullPointerException.class,
                () -> new ThemeChangeCommand(userPreferences, null, commandHandler));
    }

    @Test
    void executeInCommandHandler() {
        themeChangeCommand.execute();
        verify(commandHandler).execute(any());
    }

    @Test
    void setNewTheme() {
        Theme theme = ThemeUtils.getRandomTheme();
        themeChangeCommand.setParam(theme);
        themeChangeCommand.execute();
        verify(userPreferences).setTheme(theme);
        verify(preferencesDAO).saveOrUpdate(userPreferences);
    }

    @Test
    void setSameTheme() {
        Theme theme = userPreferences.getTheme();
        themeChangeCommand.setParam(theme);
        themeChangeCommand.execute();
        verify(userPreferences).setTheme(theme);
        verify(preferencesDAO, times(0)).saveOrUpdate(userPreferences);
    }
}