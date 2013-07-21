// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 2/18/2011 9:36:13 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SyntheticaLookAndFeel.java

package de.javasoft.plaf.synthetica;

import de.javasoft.plaf.synthetica.batik.LinearGradientPaint;
import de.javasoft.plaf.synthetica.painter.ImagePainter;
import de.javasoft.plaf.synthetica.painter.MenuPainter;
import de.javasoft.plaf.synthetica.painter.TabbedPanePainter;
import de.javasoft.plaf.synthetica.painter.TreePainter;
import de.javasoft.util.IVersion;
import de.javasoft.util.OS;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Window;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.AccessControlException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.IconUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.synth.ColorType;
import javax.swing.plaf.synth.Region;
import javax.swing.plaf.synth.SynthContext;
import javax.swing.plaf.synth.SynthLookAndFeel;
import javax.swing.plaf.synth.SynthStyle;
import javax.swing.plaf.synth.SynthStyleFactory;
import sun.swing.DefaultLookup;
import sun.swing.plaf.synth.DefaultSynthStyle;

// Referenced classes of package de.javasoft.plaf.synthetica:
//            SyntheticaRootPaneUI, SyntheticaDefaultLookup, StyleFactory, PopupFactory, 
//            AltKeyEventProcessor, SyntheticaTitledBorder

public abstract class SyntheticaLookAndFeel extends SynthLookAndFeel
{
    public static final class JVMCompatibilityMode extends Enum
    {

        public static JVMCompatibilityMode[] values()
        {
            JVMCompatibilityMode ajvmcompatibilitymode[];
            int i;
            JVMCompatibilityMode ajvmcompatibilitymode1[];
            System.arraycopy(ajvmcompatibilitymode = ENUM$VALUES, 0, ajvmcompatibilitymode1 = new JVMCompatibilityMode[i = ajvmcompatibilitymode.length], 0, i);
            return ajvmcompatibilitymode1;
        }

        public static JVMCompatibilityMode valueOf(String s)
        {
            return (JVMCompatibilityMode)Enum.valueOf(de/javasoft/plaf/synthetica/SyntheticaLookAndFeel$JVMCompatibilityMode, s);
        }

        public static final JVMCompatibilityMode COMMON;
        public static final JVMCompatibilityMode SUN;
        private static final JVMCompatibilityMode ENUM$VALUES[];

        static 
        {
            COMMON = new JVMCompatibilityMode("COMMON", 0);
            SUN = new JVMCompatibilityMode("SUN", 1);
            ENUM$VALUES = (new JVMCompatibilityMode[] {
                COMMON, SUN
            });
        }

        private JVMCompatibilityMode(String s, int i)
        {
            super(s, i);
        }
    }

    private static class Version
        implements IVersion
    {

        public int getMajor()
        {
            return major;
        }

        public int getMinor()
        {
            return minor;
        }

        public int getRevision()
        {
            return revision;
        }

        public int getBuild()
        {
            return build;
        }

        public String toString()
        {
            return (new StringBuilder(String.valueOf(major))).append(".").append(minor).append(".").append(revision).append(" Build ").append(build).toString();
        }

        private int major;
        private int minor;
        private int revision;
        private int build;

        public Version()
        {
            ResourceBundle resourcebundle = SyntheticaLookAndFeel.getResourceBundle("SyntheticaStandardLookAndFeelVersion");
            major = Integer.parseInt(resourcebundle.getString("major"));
            minor = Integer.parseInt(resourcebundle.getString("minor"));
            revision = Integer.parseInt(resourcebundle.getString("revision"));
            build = Integer.parseInt(resourcebundle.getString("build"));
        }
    }


    public SyntheticaLookAndFeel()
        throws ParseException
    {
    }

    public SyntheticaLookAndFeel(String s)
        throws ParseException
    {
        loadXMLConfig(s);
    }

    protected void loadXMLConfig(String s)
        throws ParseException
    {
        long l = System.currentTimeMillis();
        Class class1 = de/javasoft/plaf/synthetica/SyntheticaLookAndFeel;
        if(s.endsWith(".xml"))
        {
            load(class1.getResourceAsStream(s), class1);
        } else
        {
            String as[] = {
                s
            };
            String s3 = class1.getPackage().getName().replace(".", "/");
            String as1[];
            int j = (as1 = as).length;
            for(int i = 0; i < j; i++)
            {
                String s5 = as1[i];
                try
                {
                    String s6;
                    s6 = s5.startsWith("/") ? (s6 = s5.substring(1)) : (new StringBuilder(String.valueOf(s3))).append("/").append(s5).toString();
                    for(Enumeration enumeration = class1.getClassLoader().getResources(s6); enumeration.hasMoreElements();)
                    {
                        URL url = (URL)enumeration.nextElement();
                        if(url.getProtocol().equalsIgnoreCase("jar"))
                        {
                            JarURLConnection jarurlconnection = (JarURLConnection)url.openConnection();
                            JarFile jarfile = jarurlconnection.getJarFile();
                            for(Iterator iterator = Collections.list(jarfile.entries()).iterator(); iterator.hasNext();)
                            {
                                JarEntry jarentry = (JarEntry)iterator.next();
                                if(jarentry.getName().endsWith(".xml") && jarentry.getName().startsWith(s6))
                                    load(class1.getResourceAsStream((new StringBuilder("/")).append(jarentry.getName()).toString()), class1);
                            }

                        } else
                        {
                            File afile[] = (new File(url.getPath())).listFiles(new FileFilter() {

                                public boolean accept(File file1)
                                {
                                    return file1.getName().endsWith(".xml");
                                }

                                final SyntheticaLookAndFeel this$0;

            
            {
                this$0 = SyntheticaLookAndFeel.this;
                super();
            }
                            }
);
                            File afile1[];
                            int i1 = (afile1 = afile).length;
                            for(int k = 0; k < i1; k++)
                            {
                                File file = afile1[k];
                                load(file.toURI().toURL().openStream(), class1);
                            }

                        }
                    }

                }
                catch(IOException ioexception)
                {
                    throw new RuntimeException(ioexception);
                }
            }

        }
        try
        {
            String s1 = "Synthetica.xml";
            load(class1.getResourceAsStream((new StringBuilder("/")).append(s1).toString()), class1);
            if(debug)
                System.out.println((new StringBuilder("[Info] Found '")).append(s1).append("' configuration file.").toString());
        }
        catch(IllegalArgumentException illegalargumentexception) { }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
        String s2 = getClass().getName();
        try
        {
            String s4 = (new StringBuilder(String.valueOf(s2.substring(s2.lastIndexOf(".") + 1)))).append(".xml").toString();
            load(class1.getResourceAsStream((new StringBuilder("/")).append(s4).toString()), class1);
            if(debug)
                System.out.println((new StringBuilder("[Info] Found '")).append(s4).append("' configuration file.").toString());
        }
        catch(IllegalArgumentException illegalargumentexception1) { }
        catch(Exception exception1)
        {
            exception1.printStackTrace();
        }
        long l1 = System.currentTimeMillis();
        if(isSystemPropertySet("synthetica.loadTime"))
            System.out.println((new StringBuilder("Time for loading LAF: ")).append(l1 - l).append("ms").toString());
        if(isSystemPropertySet("synthetica.blockLAFChange"))
        {
            System.out.println("LAF switchings will be blocked!");
            blockLAFChange();
        }
        if(debug)
            System.out.println("Synthetica debug mode is enabled!");
        if(outputVersion)
            System.out.format("Synthetica V%s\n%s V%s\n", new Object[] {
                getSyntheticaVersion().toString(), getName(), getVersion().toString()
            });
    }

    public abstract String getID();

    public abstract String getName();

    public String getDescription()
    {
        return "Synthetica - the extended Synth Look and Feel.";
    }

    public IVersion getVersion()
    {
        ResourceBundle resourcebundle = getResourceBundle((new StringBuilder(String.valueOf(getID()))).append("Version").toString());
        final int major = Integer.parseInt(resourcebundle.getString("major"));
        final int minor = Integer.parseInt(resourcebundle.getString("minor"));
        final int revision = Integer.parseInt(resourcebundle.getString("revision"));
        final int build = Integer.parseInt(resourcebundle.getString("build"));
        return new IVersion() {

            public int getMajor()
            {
                return major;
            }

            public int getMinor()
            {
                return minor;
            }

            public int getRevision()
            {
                return revision;
            }

            public int getBuild()
            {
                return build;
            }

            public String toString()
            {
                return (new StringBuilder(String.valueOf(major))).append(".").append(minor).append(".").append(revision).append(" Build ").append(build).toString();
            }

            final SyntheticaLookAndFeel this$0;
            private final int val$major;
            private final int val$minor;
            private final int val$revision;
            private final int val$build;

            
            {
                this$0 = SyntheticaLookAndFeel.this;
                major = i;
                minor = j;
                revision = k;
                build = l;
                super();
            }
        }
;
    }

    public boolean getSupportsWindowDecorations()
    {
        return true;
    }

    public UIDefaults getDefaults()
    {
        UIDefaults uidefaults = super.getDefaults();
        return uidefaults;
    }

    private void addResourceBundleToDefaults(String s, Map map)
    {
        ResourceBundle resourcebundle = getResourceBundle(s);
        String s1;
        String s2;
        for(Enumeration enumeration = resourcebundle.getKeys(); enumeration.hasMoreElements(); map.put(s1, s2))
        {
            s1 = (String)enumeration.nextElement();
            s2 = resourcebundle.getString(s1);
        }

    }

    public void initialize()
    {
        super.initialize();
        orgDefaults = (UIDefaults)UIManager.getDefaults().clone();
        if(getJVMCompatibilityMode() == JVMCompatibilityMode.SUN)
            try
            {
                Class class1 = Class.forName("sun.swing.DefaultLookup");
                Method method = class1.getMethod("setDefaultLookup", new Class[] {
                    sun/swing/DefaultLookup
                });
                method.invoke(class1, new Object[] {
                    new SyntheticaDefaultLookup()
                });
            }
            catch(Exception exception)
            {
                exception.printStackTrace();
            }
        StyleFactory stylefactory = new StyleFactory(getStyleFactory());
        SynthLookAndFeel.setStyleFactory(stylefactory);
        PopupFactory.install();
        lafChangeListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent propertychangeevent)
            {
                SyntheticaLookAndFeel.setFont(null, 0);
                reinit();
                installSyntheticaDefaults();
                if(SyntheticaLookAndFeel.defaultsCompatibilityMode)
                    installCompatibilityDefaults();
                SyntheticaLookAndFeel.updateAllWindowShapes();
            }

            final SyntheticaLookAndFeel this$0;

            
            {
                this$0 = SyntheticaLookAndFeel.this;
                super();
            }
        }
;
        PropertyChangeListener apropertychangelistener[] = UIManager.getPropertyChangeListeners();
        PropertyChangeListener apropertychangelistener1[];
        int k = (apropertychangelistener1 = apropertychangelistener).length;
        for(int i = 0; i < k; i++)
        {
            PropertyChangeListener propertychangelistener = apropertychangelistener1[i];
            UIManager.removePropertyChangeListener(propertychangelistener);
        }

        UIManager.addPropertyChangeListener(lafChangeListener);
        k = (apropertychangelistener1 = apropertychangelistener).length;
        for(int j = 0; j < k; j++)
        {
            PropertyChangeListener propertychangelistener1 = apropertychangelistener1[j];
            UIManager.addPropertyChangeListener(propertychangelistener1);
        }

    }

    private void reinit()
    {
        MenuPainter.reinitialize();
        TreePainter.reinitialize();
        TabbedPanePainter.reinitialize();
        ImagePainter.clearImageCache();
    }

    protected void installSyntheticaDefaults()
    {
        uiDefaults = UIManager.getDefaults();
        uiDefaults.put("HyperlinkUI", "de.javasoft.plaf.synthetica.HyperlinkUI");
        uiDefaults.put("StatusBarUI", "de.javasoft.plaf.synthetica.StatusBarUI");
        uiDefaults.put("LoginPanelUI", "de.javasoft.plaf.synthetica.LoginPanelUI");
        uiDefaults.put("LoginPaneUI", "de.javasoft.plaf.synthetica.LoginPanelUI");
        uiDefaults.put("MonthViewUI", "de.javasoft.plaf.synthetica.MonthViewUI");
        uiDefaults.put("TitledPanelUI", "de.javasoft.plaf.synthetica.TitledPanelUI");
        uiDefaults.put("HeaderUI", "de.javasoft.plaf.synthetica.HeaderUI");
        uiDefaults.put("swingx/GroupableTableHeaderUI", "de.javasoft.plaf.synthetica.GroupableTableHeaderUI");
        uiDefaults.put("swingx/TaskPaneUI", "de.javasoft.plaf.synthetica.TaskPaneUI");
        uiDefaults.put("swingx/TaskPaneContainerUI", "de.javasoft.plaf.synthetica.TaskPaneContainerUI");
        uiDefaults.put("swingx/TipOfTheDayUI", "de.javasoft.plaf.synthetica.TipOfTheDayUI");
        uiDefaults.put("Flexdock.view", "de.javasoft.plaf.synthetica.flexdock.ViewUI");
        uiDefaults.put("Flexdock.titlebar", "de.javasoft.plaf.synthetica.flexdock.TitlebarUI");
        uiDefaults.put("Flexdock.titlebar.button", "de.javasoft.plaf.synthetica.flexdock.ButtonUI");
        addResourceBundleToDefaults("synthetica", uiDefaults);
        if(UIManager.getBoolean("Synthetica.window.decoration"))
            uiDefaults.put("RootPaneUI", "de.javasoft.plaf.synthetica.SyntheticaRootPaneUI");
        else
            decorated = false;
        JFrame.setDefaultLookAndFeelDecorated(decorated);
        JDialog.setDefaultLookAndFeelDecorated(decorated);
        extendedFileChooserEnabled = UIManager.getBoolean("Synthetica.extendedFileChooser.enabled");
        setExtendedFileChooserEnabled(extendedFileChooserEnabled);
        rememberFileChooserPreferences = UIManager.getBoolean("Synthetica.extendedFileChooser.rememberPreferences");
        useSystemFileIcons = UIManager.getBoolean("Synthetica.extendedFileChooser.useSystemFileIcons");
        UIDefaults uidefaults = UIManager.getLookAndFeelDefaults();
        for(Iterator iterator = uidefaults.entrySet().iterator(); iterator.hasNext();)
        {
            java.util.Map.Entry entry = (java.util.Map.Entry)iterator.next();
            if(!uiDefaults.containsKey(entry.getKey()))
                uiDefaults.put(entry.getKey(), entry.getValue());
        }

        if(uiDefaults.get("Synthetica.activateMenuByAltKey") != null)
            if(!((Boolean)uiDefaults.get("Synthetica.activateMenuByAltKey")).booleanValue());
        altKeyEventProcessor = new AltKeyEventProcessor();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventPostProcessor(altKeyEventProcessor);
    }

    protected void installCompatibilityDefaults()
    {
        UIDefaults uidefaults = UIManager.getDefaults();
        initSystemColorDefaults(UIManager.getDefaults());
        Object aobj[] = {
            "OptionPane.errorSound", "OptionPane.informationSound", "OptionPane.questionSound", "OptionPane.warningSound", "InternalFrame.closeSound", "InternalFrame.maximizeSound", "InternalFrame.minimizeSound", "InternalFrame.restoreDownSound", "InternalFrame.restoreUpSound", "PopupMenu.popupSound", 
            "MenuItem.commandSound", "CheckBoxMenuItem.commandSound", "RadioButtonMenuItem.commandSound"
        };
        Object aobj1[] = {
            "FileChooser.usesSingleFilePane", Boolean.valueOf(true), "FileChooser.ancestorInputMap", new javax.swing.UIDefaults.LazyInputMap(new Object[] {
                "ESCAPE", "cancelSelection", "F2", "editFileName", "F5", "refresh", "ENTER", "approveSelection", "BACK_SPACE", "Go Up"
            }), "List.selectionForeground", new ColorUIResource(Color.white), "SplitPane.dividerSize", Integer.valueOf(8), "List.focusCellHighlightBorder", new javax.swing.plaf.BorderUIResource.LineBorderUIResource(uidefaults.getColor("Synthetica.list.focusCellHighlightBorder.color")), 
            "Table.focusCellHighlightBorder", new javax.swing.plaf.BorderUIResource.LineBorderUIResource(uidefaults.getColor("Synthetica.table.focusCellHighlightBorder.color")), "Table.scrollPaneBorder", new javax.swing.plaf.BorderUIResource.LineBorderUIResource(uidefaults.getColor("Synthetica.table.scrollPane.border.color")), "TitledBorder.border", new BorderUIResource(new SyntheticaTitledBorder()), "RootPane.defaultButtonWindowKeyBindings", new Object[] {
                "ENTER", "press", "released ENTER", "release", "ctrl ENTER", "press", "ctrl released ENTER", "release"
            }, "Table.ancestorInputMap", new javax.swing.UIDefaults.LazyInputMap(new Object[] {
                "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", 
                "CUT", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", 
                "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", 
                "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", 
                "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", 
                "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", 
                "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", 
                "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME", "selectFirstColumnExtendSelection", 
                "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", 
                "ctrl END", "selectLastRow", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", 
                "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB", "selectNextColumnCell", 
                "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell", "shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", 
                "ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", 
                "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo"
            }), 
            "Table.ancestorInputMap.RightToLeft", new javax.swing.UIDefaults.LazyInputMap(new Object[] {
                "RIGHT", "selectPreviousColumn", "KP_RIGHT", "selectPreviousColumn", "shift RIGHT", "selectPreviousColumnExtendSelection", "shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift RIGHT", "selectPreviousColumnExtendSelection", 
                "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection", "shift RIGHT", "selectPreviousColumnChangeLead", "shift KP_RIGHT", "selectPreviousColumnChangeLead", "LEFT", "selectNextColumn", "KP_LEFT", "selectNextColumn", 
                "shift LEFT", "selectNextColumnExtendSelection", "shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl shift LEFT", "selectNextColumnExtendSelection", "ctrl shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl LEFT", "selectNextColumnChangeLead", 
                "ctrl KP_LEFT", "selectNextColumnChangeLead", "ctrl PAGE_UP", "scrollRightChangeSelection", "ctrl PAGE_DOWN", "scrollLeftChangeSelection", "ctrl shift PAGE_UP", "scrollRightExtendSelection", "ctrl shift PAGE_DOWN", "scrollLeftExtendSelection"
            }), "ComboBox.ancestorInputMap", new javax.swing.UIDefaults.LazyInputMap(new Object[] {
                "ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", 
                "DOWN", "selectNext", "KP_DOWN", "selectNext", "alt DOWN", "togglePopup", "alt KP_DOWN", "togglePopup", "alt UP", "togglePopup", 
                "alt KP_UP", "togglePopup", "SPACE", "spacePopup", "ENTER", "enterPressed", "UP", "selectPrevious", "KP_UP", "selectPrevious"
            }), "controlLtHighlight", new ColorUIResource(Color.WHITE), "controlHighlight", new ColorUIResource(Color.LIGHT_GRAY), "controlShadow", new ColorUIResource(Color.DARK_GRAY), 
            "controlDkShadow", new ColorUIResource(Color.BLACK), "ScrollBar.minimumThumbSize", new DimensionUIResource(8, 8), "ScrollBar.maximumThumbSize", new DimensionUIResource(4096, 4096), "AuditoryCues.cueList", aobj, "AuditoryCues.defaultCueList", new Object[] {
                "OptionPane.informationSound", "OptionPane.warningSound", "OptionPane.questionSound", "OptionPane.errorSound"
            }, 
            "AuditoryCues.allAuditoryCues", aobj, "AuditoryCues.noAuditoryCues", new Object[] {
                "mute"
            }, "OptionPane.informationSound", "/javax/swing/plaf/metal/sounds/OptionPaneInformation.wav", "OptionPane.warningSound", "/javax/swing/plaf/metal/sounds/OptionPaneWarning.wav", "OptionPane.errorSound", "/javax/swing/plaf/metal/sounds/OptionPaneError.wav", 
            "OptionPane.questionSound", "/javax/swing/plaf/metal/sounds/OptionPaneQuestion.wav", "InternalFrame.closeSound", "/javax/swing/plaf/metal/sounds/FrameClose.wav", "InternalFrame.maximizeSound", "/javax/swing/plaf/metal/sounds/FrameMaximize.wav", "InternalFrame.minimizeSound", "/javax/swing/plaf/metal/sounds/FrameMinimize.wav", "InternalFrame.restoreDownSound", "/javax/swing/plaf/metal/sounds/FrameRestoreDown.wav", 
            "InternalFrame.restoreUpSound", "/javax/swing/plaf/metal/sounds/FrameRestoreUp.wav", "MenuItem.commandSound", "/javax/swing/plaf/metal/sounds/MenuItemCommand.wav", "PopupMenu.popupSound", "/javax/swing/plaf/metal/sounds/PopupMenuPopup.wav", "CheckBoxMenuItem.commandSound", "/javax/swing/plaf/metal/sounds/MenuItemCommand.wav", "RadioButtonMenuItem.commandSound", "/javax/swing/plaf/metal/sounds/MenuItemCommand.wav"
        };
        uidefaults.putDefaults(aobj1);
        SynthStyle synthstyle = null;
        SynthContext synthcontext = null;
        SynthStyleFactory synthstylefactory = getStyleFactory();
        String as[] = (String[])null;
        String as1[] = (String[])null;
        Font font = null;
        JButton jbutton = new JButton();
        synthstyle = synthstylefactory.getStyle(jbutton, Region.BUTTON);
        synthcontext = new SynthContext(jbutton, Region.BUTTON, synthstyle, 1);
        font = synthstyle.getFont(synthcontext);
        uidefaults.put("Button.font", font);
        uidefaults.put("Button.textShiftOffset", Integer.valueOf(synthstyle.getInt(synthcontext, "Button.textShiftOffset", 0)));
        uidefaults.put("Button.foreground", synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND));
        synthcontext = new SynthContext(jbutton, Region.BUTTON, synthstyle, 8);
        uidefaults.put("Button.disabledForeground", synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND));
        JComboBox jcombobox = new JComboBox();
        synthstyle = synthstylefactory.getStyle(jcombobox, Region.COMBO_BOX);
        synthcontext = new SynthContext(jcombobox, Region.COMBO_BOX, synthstyle, 1024);
        Color color = synthstyle.getColor(synthcontext, ColorType.BACKGROUND);
        uidefaults.put("ComboBox.background", color);
        Color color1 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("ComboBox.foreground", color1);
        uidefaults.put("ComboBox.font", synthstyle.getFont(synthcontext));
        JLabel jlabel = new JLabel();
        synthstyle = synthstylefactory.getStyle(jlabel, Region.LABEL);
        synthcontext = new SynthContext(jlabel, Region.LABEL, synthstyle, 1024);
        font = synthstyle.getFont(synthcontext);
        uidefaults.put("Label.font", font);
        uidefaults.put("JXMonthView.font", font);
        uidefaults.put("JXTitledPanel.titleFont", font.deriveFont(1));
        Color color2 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("Label.foreground", color2);
        synthcontext = new SynthContext(jlabel, Region.LABEL, synthstyle, 8);
        color2 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("Label.disabledForeground", color2);
        JPanel jpanel = new JPanel();
        synthstyle = synthstylefactory.getStyle(jpanel, Region.PANEL);
        synthcontext = new SynthContext(jpanel, Region.PANEL, synthstyle, 1024);
        Color color3 = synthstyle.getColor(synthcontext, ColorType.BACKGROUND);
        uidefaults.put("Panel.background", color3);
        uidefaults.put("SplitPane.background", color3);
        uidefaults.put("Label.background", color3);
        uidefaults.put("ColorChooser.swatchesDefaultRecentColor", color3);
        uidefaults.put("control", color3);
        Color color4 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("Panel.foreground", color4);
        font = synthstyle.getFont(synthcontext);
        uidefaults.put("Panel.font", font);
        uidefaults.put("TitledBorder.font", getTitledBorderFont(font));
        JList jlist = new JList();
        synthstyle = synthstylefactory.getStyle(jlist, Region.LIST);
        synthcontext = new SynthContext(jlist, Region.LIST, synthstyle, 1024);
        Color color5 = synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND);
        uidefaults.put("List.background", color5);
        Color color6 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("List.foreground", color6);
        synthcontext = new SynthContext(jlist, Region.LIST, synthstyle, 512);
        color5 = synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND);
        uidefaults.put("List.selectionBackground", color5);
        color6 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("List.selectionForeground", color6);
        JTable jtable = new JTable();
        synthstyle = synthstylefactory.getStyle(jtable, Region.TABLE_HEADER);
        synthcontext = new SynthContext(jtable, Region.TABLE_HEADER, synthstyle, 1024);
        Color color7 = synthstyle.getColor(synthcontext, ColorType.BACKGROUND);
        uidefaults.put("TableHeader.background", color7);
        Color color8 = synthstyle.getColor(synthcontext, ColorType.FOREGROUND);
        uidefaults.put("TableHeader.foreground", color8);
        uidefaults.put("TableHeader.font", synthstyle.getFont(synthcontext));
        synthstyle = synthstylefactory.getStyle(jtable, Region.TABLE);
        synthcontext = new SynthContext(jtable, Region.TABLE, synthstyle, 1024);
        uidefaults.put("Table.gridColor", synthstyle.get(synthcontext, "Table.gridColor"));
        Color color9 = synthstyle.getColor(synthcontext, ColorType.BACKGROUND);
        uidefaults.put("Table.background", color9);
        Color color10 = synthstyle.getColor(synthcontext, ColorType.FOREGROUND);
        uidefaults.put("Table.foreground", color10);
        uidefaults.put("Table.font", synthstyle.getFont(synthcontext));
        synthcontext = new SynthContext(jtable, Region.TABLE, synthstyle, 512);
        color9 = synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND);
        uidefaults.put("Table.selectionBackground", color9);
        color10 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("Table.selectionForeground", color10);
        uidefaults.put("Table.ascendingSortIcon", loadIcon("Synthetica.arrow.up"));
        uidefaults.put("Table.descendingSortIcon", loadIcon("Synthetica.arrow.down"));
        if(uidefaults.getBoolean("Synthetica.table.useScrollPaneBorder"))
            uidefaults.put("Table.scrollPaneBorder", (new JScrollPane()).getBorder());
        JTree jtree = new JTree();
        synthstyle = synthstylefactory.getStyle(jtree, Region.TREE);
        if(getJVMCompatibilityMode() == JVMCompatibilityMode.SUN)
        {
            font = ((DefaultSynthStyle)synthstyle).getFont(jtree, Region.TREE, 1);
            uidefaults.put("Tree.font", font);
        }
        synthcontext = new SynthContext(jtree, Region.TREE, synthstyle, 1024);
        as1 = (new String[] {
            "Tree.expandedIcon", "Tree.collapsedIcon"
        });
        putIcons2Defaults(uidefaults, as1, as1, synthstyle, synthcontext);
        uidefaults.put("Tree.rowHeight", synthstyle.get(synthcontext, "Tree.rowHeight"));
        uidefaults.put("Tree.leftChildIndent", synthstyle.get(synthcontext, "Tree.leftChildIndent"));
        uidefaults.put("Tree.rightChildIndent", synthstyle.get(synthcontext, "Tree.rightChildIndent"));
        synthstyle = synthstylefactory.getStyle(jtree, Region.TREE_CELL);
        synthcontext = new SynthContext(jtree, Region.TREE_CELL, synthstyle, 1024);
        uidefaults.put("Tree.textForeground", synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND));
        uidefaults.put("Tree.textBackground", synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND));
        synthcontext = new SynthContext(jtree, Region.TREE_CELL, synthstyle, 512);
        uidefaults.put("Tree.selectionForeground", synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND));
        uidefaults.put("Tree.selectionBackground", synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND));
        uidefaults.put("Tree.hash", uidefaults.get("Synthetica.tree.line.color.vertical"));
        JInternalFrame jinternalframe = new JInternalFrame();
        synthstyle = synthstylefactory.getStyle(jinternalframe, Region.INTERNAL_FRAME_TITLE_PANE);
        if(getJVMCompatibilityMode() == JVMCompatibilityMode.SUN)
        {
            Color color11 = ((DefaultSynthStyle)synthstyle).getColor(jinternalframe, Region.INTERNAL_FRAME_TITLE_PANE, 512, ColorType.FOREGROUND);
            uidefaults.put("InternalFrame.activeTitleForeground", color11);
            color11 = ((DefaultSynthStyle)synthstyle).getColor(jinternalframe, Region.INTERNAL_FRAME_TITLE_PANE, 1024, ColorType.FOREGROUND);
            uidefaults.put("InternalFrame.inactiveTitleForeground", color11);
            Color color12 = ((DefaultSynthStyle)synthstyle).getColor(jinternalframe, Region.INTERNAL_FRAME_TITLE_PANE, 512, ColorType.BACKGROUND);
            uidefaults.put("InternalFrame.activeTitleBackground", color12);
            uidefaults.put("activeCaption", color12);
            color12 = ((DefaultSynthStyle)synthstyle).getColor(jinternalframe, Region.INTERNAL_FRAME_TITLE_PANE, 1024, ColorType.BACKGROUND);
            uidefaults.put("InternalFrame.inactiveTitleBackground", color12);
            uidefaults.put("inactiveCaption", color12);
        }
        synthcontext = new SynthContext(jinternalframe, Region.INTERNAL_FRAME_TITLE_PANE, synthstyle, 1024);
        as = (new String[] {
            "InternalFrameTitlePane.closeIcon", "InternalFrameTitlePane.maximizeIcon", "InternalFrameTitlePane.minimizeIcon", "InternalFrameTitlePane.iconifyIcon"
        });
        as1 = (new String[] {
            "InternalFrame.closeIcon", "InternalFrame.maximizeIcon", "InternalFrame.minimizeIcon", "InternalFrame.iconifyIcon"
        });
        putIcons2Defaults(uidefaults, as, as1, synthstyle, synthcontext);
        synthstyle = synthstylefactory.getStyle(jinternalframe, Region.INTERNAL_FRAME);
        synthcontext = new SynthContext(jinternalframe, Region.INTERNAL_FRAME, synthstyle, 1024);
        as1 = (new String[] {
            "InternalFrame.icon"
        });
        putIcons2Defaults(uidefaults, as1, as1, synthstyle, synthcontext);
        JMenu jmenu = new JMenu();
        synthstyle = synthstylefactory.getStyle(jmenu, Region.MENU);
        synthcontext = new SynthContext(jmenu, Region.MENU, synthstyle, 1024);
        uidefaults.put("MenuItem.background", synthstyle.getColor(synthcontext, ColorType.BACKGROUND));
        Color color13 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("Menu.foreground", color13);
        uidefaults.put("MenuItem.foreground", color13);
        JOptionPane joptionpane = new JOptionPane();
        synthstyle = synthstylefactory.getStyle(joptionpane, Region.OPTION_PANE);
        synthcontext = new SynthContext(joptionpane, Region.OPTION_PANE, synthstyle, 1024);
        as1 = (new String[] {
            "OptionPane.informationIcon", "OptionPane.questionIcon", "OptionPane.warningIcon", "OptionPane.errorIcon"
        });
        putIcons2Defaults(uidefaults, as1, as1, synthstyle, synthcontext);
        JCheckBox jcheckbox = new JCheckBox();
        synthstyle = synthstylefactory.getStyle(jcheckbox, Region.CHECK_BOX);
        synthcontext = new SynthContext(jcheckbox, Region.CHECK_BOX, synthstyle, 1024);
        as1 = (new String[] {
            "CheckBox.icon"
        });
        putIcons2Defaults(uidefaults, as1, as1, synthstyle, synthcontext);
        JRadioButton jradiobutton = new JRadioButton();
        synthstyle = synthstylefactory.getStyle(jradiobutton, Region.RADIO_BUTTON);
        synthcontext = new SynthContext(jradiobutton, Region.RADIO_BUTTON, synthstyle, 1024);
        as1 = (new String[] {
            "RadioButton.icon"
        });
        putIcons2Defaults(uidefaults, as1, as1, synthstyle, synthcontext);
        JTabbedPane jtabbedpane = new JTabbedPane();
        synthstyle = synthstylefactory.getStyle(jtabbedpane, Region.TABBED_PANE_TAB_AREA);
        synthcontext = new SynthContext(jtabbedpane, Region.TABBED_PANE_TAB_AREA, synthstyle, 1024);
        uidefaults.put("TabbedPane.tabAreaInsets", synthstyle.getInsets(synthcontext, null));
        synthstyle = synthstylefactory.getStyle(jtabbedpane, Region.TABBED_PANE_TAB);
        synthcontext = new SynthContext(jtabbedpane, Region.TABBED_PANE_TAB, synthstyle, 1024);
        uidefaults.put("TabbedPane.tabInsets", synthstyle.getInsets(synthcontext, null));
        synthstyle = synthstylefactory.getStyle(jtabbedpane, Region.TABBED_PANE_TAB);
        synthcontext = new SynthContext(jtabbedpane, Region.TABBED_PANE_TAB, synthstyle, 512);
        uidefaults.put("TabbedPane.selectedTabPadInsets", synthstyle.getInsets(synthcontext, null));
        synthstyle = synthstylefactory.getStyle(jtabbedpane, Region.TABBED_PANE_CONTENT);
        synthcontext = new SynthContext(jtabbedpane, Region.TABBED_PANE_CONTENT, synthstyle, 1024);
        uidefaults.put("TabbedPane.contentBorderInsets", synthstyle.getInsets(synthcontext, null));
        uidefaults.put("TabbedPane.foreground", synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND));
        uidefaults.put("TabbedPane.shadow", Color.GRAY);
        JTextField jtextfield = new JTextField();
        uidefaults.put("TextField.border", jtextfield.getBorder());
        synthstyle = synthstylefactory.getStyle(jtextfield, Region.TEXT_FIELD);
        synthcontext = new SynthContext(jtextfield, Region.TEXT_FIELD, synthstyle, 1024);
        font = synthstyle.getFont(synthcontext);
        uidefaults.put("TextField.font", font);
        uidefaults.put("FormattedTextField.font", font);
        uidefaults.put("PasswordField.font", font);
        uidefaults.put("TextArea.font", font);
        Color color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("TextField.foreground", color14);
        uidefaults.put("FormattedTextField.foreground", color14);
        uidefaults.put("PasswordField.foreground", color14);
        uidefaults.put("TextArea.foreground", color14);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND);
        uidefaults.put("TextField.background", color14);
        uidefaults.put("FormattedTextField.background", color14);
        uidefaults.put("PasswordField.background", color14);
        uidefaults.put("TextArea.background", color14);
        synthcontext = new SynthContext(jtextfield, Region.TEXT_FIELD, synthstyle, 8);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("TextField.inactiveForeground", color14);
        uidefaults.put("FormattedTextField.inactiveForeground", color14);
        uidefaults.put("PasswordField.inactiveForeground", color14);
        uidefaults.put("TextArea.inactiveForeground", color14);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND);
        uidefaults.put("TextField.inactiveBackground", color14);
        uidefaults.put("FormattedTextField.inactiveBackground", color14);
        uidefaults.put("PasswordField.inactiveBackground", color14);
        uidefaults.put("TextArea.inactiveBackground", color14);
        synthcontext = new SynthContext(jtextfield, Region.TEXT_FIELD, synthstyle, 512);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("TextField.selectionForeground", color14);
        uidefaults.put("FormattedTextField.selectionForeground", color14);
        uidefaults.put("PasswordField.selectionForeground", color14);
        uidefaults.put("TextArea.selectionForeground", color14);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND);
        uidefaults.put("TextField.selectionBackground", color14);
        uidefaults.put("FormattedTextField.selectionBackground", color14);
        uidefaults.put("PasswordField.selectionBackground", color14);
        uidefaults.put("TextArea.selectionBackground", color14);
        uidefaults.put("textHighlight", color14);
        JTextPane jtextpane = new JTextPane();
        synthstyle = synthstylefactory.getStyle(jtextfield, Region.TEXT_PANE);
        synthcontext = new SynthContext(jtextpane, Region.TEXT_PANE, synthstyle, 1024);
        font = synthstyle.getFont(synthcontext);
        uidefaults.put("TextPane.font", font);
        uidefaults.put("EditorPane.font", font);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("TextPane.foreground", color14);
        uidefaults.put("EditorPane.foreground", color14);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND);
        uidefaults.put("TextPane.background", color14);
        uidefaults.put("EditorPane.background", color14);
        synthcontext = new SynthContext(jtextpane, Region.TEXT_PANE, synthstyle, 8);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("TextPane.inactiveForeground", color14);
        uidefaults.put("EditorPane.inactiveForeground", color14);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND);
        uidefaults.put("TextPane.inactiveBackground", color14);
        uidefaults.put("EditorPane.inactiveBackground", color14);
        synthcontext = new SynthContext(jtextpane, Region.TEXT_PANE, synthstyle, 512);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND);
        uidefaults.put("TextPane.selectionForeground", color14);
        uidefaults.put("EditorPane.selectionForeground", color14);
        color14 = synthstyle.getColor(synthcontext, ColorType.TEXT_BACKGROUND);
        uidefaults.put("TextPane.selectionBackground", color14);
        uidefaults.put("EditorPane.selectionBackground", color14);
        JToolTip jtooltip = new JToolTip();
        synthcontext = new SynthContext(jtooltip, Region.TOOL_TIP, synthstyle, 1024);
        synthstyle = synthstylefactory.getStyle(jtooltip, Region.TOOL_TIP);
        uidefaults.put("ToolTip.font", synthstyle.getFont(synthcontext));
        uidefaults.put("ToolTip.foreground", synthstyle.getColor(synthcontext, ColorType.TEXT_FOREGROUND));
        uidefaults.put("ToolTip.background", synthstyle.getColor(synthcontext, ColorType.BACKGROUND));
        uidefaults.put("ColumnHeaderRenderer.upIcon", loadIcon("Synthetica.arrow.up"));
        uidefaults.put("ColumnHeaderRenderer.downIcon", loadIcon("Synthetica.arrow.down"));
    }

    private void putIcons2Defaults(UIDefaults uidefaults, String as[], String as1[], SynthStyle synthstyle, SynthContext synthcontext)
    {
        for(int i = 0; i < as.length; i++)
        {
            Icon icon = synthstyle.getIcon(synthcontext, as[i]);
            uidefaults.put(as1[i], icon);
        }

    }

    private Font getTitledBorderFont(Font font)
    {
        if(UIManager.get("Synthetica.titledBorder.title.fontName") != null)
            font = new FontUIResource(new Font(UIManager.getString("Synthetica.titledBorder.title.fontName"), font.getStyle(), font.getSize()));
        if(UIManager.get("Synthetica.titledBorder.title.fontSize") != null)
            font = new FontUIResource(font.deriveFont(UIManager.getInt("Synthetica.titledBorder.title.fontSize")));
        if(UIManager.get("Synthetica.titledBorder.title.fontStyle") != null)
            font = new FontUIResource(font.deriveFont(UIManager.getInt("Synthetica.titledBorder.title.fontStyle")));
        return font;
    }

    public void uninitialize()
    {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventPostProcessor(altKeyEventProcessor);
        ((StyleFactory)SynthLookAndFeel.getStyleFactory()).uninitialize();
        UIDefaults uidefaults = UIManager.getDefaults();
        Object obj = UIManager.get("Synthetica.license.info");
        Object obj1 = UIManager.get("Synthetica.license.key");
        Object obj2 = UIManager.get("SyntheticaAddons.license.info");
        Object obj3 = UIManager.get("SyntheticaAddons.license.key");
        uidefaults.clear();
        java.util.Map.Entry entry;
        for(Iterator iterator = orgDefaults.entrySet().iterator(); iterator.hasNext(); uidefaults.put(entry.getKey(), entry.getValue()))
            entry = (java.util.Map.Entry)iterator.next();

        if(obj != null)
        {
            UIManager.put("Synthetica.license.info", obj);
            UIManager.put("Synthetica.license.key", obj1);
        }
        if(obj2 != null)
        {
            uidefaults.put("SyntheticaAddons.license.info", obj2);
            uidefaults.put("SyntheticaAddons.license.key", obj3);
        }
        UIManager.removePropertyChangeListener(lafChangeListener);
        super.uninitialize();
    }

    private void blockLAFChange()
    {
        UIManager.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent propertychangeevent)
            {
                Object obj = propertychangeevent.getNewValue();
                if(!(obj instanceof SyntheticaLookAndFeel))
                    try
                    {
                        UIManager.setLookAndFeel(SyntheticaLookAndFeel.this);
                    }
                    catch(Exception exception)
                    {
                        exception.printStackTrace();
                    }
            }

            final SyntheticaLookAndFeel this$0;

            
            {
                this$0 = SyntheticaLookAndFeel.this;
                super();
            }
        }
);
    }

    public static SynthContext createContext(JComponent jcomponent, Region region, int i)
    {
        SynthStyle synthstyle = getStyle(jcomponent, region);
        return new SynthContext(jcomponent, region, synthstyle, i);
    }

    public static void setFont(String s, int i)
    {
        fontName = s;
        fontSize = i;
    }

    public static String getFontName()
    {
        return fontName;
    }

    public static int getFontSize()
    {
        return fontSize;
    }

    public static boolean getAntiAliasEnabled()
    {
        return antiAliasEnabled;
    }

    public static void setAntiAliasEnabled(boolean flag)
    {
        antiAliasEnabled = flag;
        System.setProperty("awt.useSystemAAFontSettings", "false");
    }

    public static void setWindowsDecorated(boolean flag)
    {
        decorated = flag;
    }

    public static boolean getExtendedFileChooserEnabled()
    {
        return extendedFileChooserEnabled;
    }

    public static void setExtendedFileChooserEnabled(boolean flag)
    {
        extendedFileChooserEnabled = flag;
        if(extendedFileChooserEnabled)
        {
            UIDefaults uidefaults = UIManager.getDefaults();
            String s = null;
            if(OS.getCurrentOS() == OS.Mac)
            {
                String s1 = "ch.randelshofer.quaqua.panther.QuaquaPantherFileChooserUI";
                s = uidefaults.get("Synthetica.fileChooserUI") != null ? uidefaults.getString("Synthetica.fileChooserUI") : s1;
                try
                {
                    Class.forName(s);
                    initQuaquaFileChooser(uidefaults);
                }
                catch(ClassNotFoundException classnotfoundexception)
                {
                    s = null;
                }
            }
            s = s != null ? s : "de.javasoft.plaf.synthetica.filechooser.SyntheticaFileChooserUI";
            uidefaults.put("FileChooserUI", s);
        } else
        {
            UIManager.getDefaults().put("FileChooserUI", "javax.swing.plaf.metal.MetalFileChooserUI");
        }
    }

    private static void initQuaquaFileChooser(UIDefaults uidefaults)
    {
        uidefaults.addResourceBundle("ch.randelshofer.quaqua.Labels");
        uidefaults.put("Browser.selectionBackground", new ColorUIResource(56, 117, 215));
        uidefaults.put("Browser.selectionForeground", new ColorUIResource(255, 255, 255));
        uidefaults.put("Browser.inactiveSelectionBackground", new ColorUIResource(208, 208, 208));
        uidefaults.put("Browser.inactiveSelectionForeground", new ColorUIResource(0, 0, 0));
        uidefaults.put("FileChooser.previewLabelForeground", new ColorUIResource(0));
        uidefaults.put("FileChooser.previewValueForeground", new ColorUIResource(0));
        FontUIResource fontuiresource = new FontUIResource("Lucida Grande", 0, 11);
        uidefaults.put("FileChooser.previewLabelFont", fontuiresource);
        uidefaults.put("FileChooser.previewValueFont", fontuiresource);
        uidefaults.put("FileChooser.previewLabelInsets", new InsetsUIResource(0, 0, 0, 4));
        uidefaults.put("FileChooser.cellTipOrigin", new Point(18, 1));
        uidefaults.put("FileChooser.autovalidate", Boolean.TRUE);
        uidefaults.put("Sheet.showAsSheet", Boolean.TRUE);
    }

    public Icon getDisabledIcon(JComponent jcomponent, Icon icon)
    {
        if(icon == null || !getBoolean("Synthetica.translucency4DisabledIcons.enabled", jcomponent))
        {
            return super.getDisabledIcon(jcomponent, icon);
        } else
        {
            int i = getInt("Synthetica.translucency4DisabledIcons.alpha", jcomponent, 50);
            BufferedImage bufferedimage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), 2);
            Graphics2D graphics2d = (Graphics2D)bufferedimage.getGraphics();
            AlphaComposite alphacomposite = AlphaComposite.getInstance(3, (float)i / 100F);
            graphics2d.setComposite(alphacomposite);
            icon.paintIcon(null, graphics2d, 0, 0);
            graphics2d.dispose();
            return new ImageIcon(bufferedimage);
        }
    }

    public static boolean getRememberFileChooserPreferences()
    {
        return rememberFileChooserPreferences;
    }

    public static void setRememberFileChooserPreferences(boolean flag)
    {
        rememberFileChooserPreferences = flag;
    }

    public static boolean getUseSystemFileIcons()
    {
        return useSystemFileIcons;
    }

    public static void setUseSystemFileIcons(boolean flag)
    {
        useSystemFileIcons = flag;
    }

    public static void setDefaultsCompatibilityMode(boolean flag)
    {
        defaultsCompatibilityMode = flag;
    }

    public static boolean getDefaultsCompatibilityMode()
    {
        return defaultsCompatibilityMode;
    }

    public static void setToolbarSeparatorDimension(Dimension dimension)
    {
        toolbarSeparatorDimension = dimension;
    }

    public static Dimension getToolbarSeparatorDimension()
    {
        return toolbarSeparatorDimension;
    }

    private static ResourceBundle getResourceBundle(String s)
    {
        return ResourceBundle.getBundle((new StringBuilder("de/javasoft/plaf/synthetica/resourceBundles/")).append(s).toString());
    }

    public IVersion getSyntheticaVersion()
    {
        return new Version();
    }

    public static JVMCompatibilityMode getJVMCompatibilityMode()
    {
        String s = UIManager.getString("Synthetica.JVMCompatibilityMode");
        if(s == null)
            return JVMCompatibilityMode.SUN;
        else
            return JVMCompatibilityMode.valueOf(s);
    }

    public static Icon loadIcon(String s)
    {
        return loadIcon(s, null);
    }

    public static Icon loadIcon(String s, Component component)
    {
        Object obj = null;
        String s1 = getString(s, component);
        if(s1 != null)
        {
            URL url = de/javasoft/plaf/synthetica/SyntheticaLookAndFeel.getResource(s1);
            if(url == null)
                try
                {
                    obj = (Icon)Class.forName(s1).newInstance();
                }
                catch(Exception exception)
                {
                    throw new RuntimeException(exception);
                }
            else
                obj = new ImageIcon(url);
            obj = new IconUIResource(((Icon) (obj)));
        }
        return ((Icon) (obj));
    }

    public static Object get(String s, String s1, String s2, boolean flag)
    {
        String s3 = s;
        char c = '.';
        for(int i = s.length(); i > -1;)
        {
            StringBuilder stringbuilder = new StringBuilder("Synthetica.");
            s = s.substring(0, i);
            stringbuilder.append(s);
            if(s1 != null)
            {
                stringbuilder.append(c);
                stringbuilder.append(s1);
            }
            if(s2 != null)
            {
                stringbuilder.append(c);
                stringbuilder.append(s2);
            }
            if(lookup(stringbuilder.toString()) != null || !flag)
                return lookup(stringbuilder.toString());
            i = s.lastIndexOf(c);
            if(i == -1 && s2 != null)
            {
                s2 = null;
                s = s3;
                i = s.length();
            }
        }

        return null;
    }

    public static String getString(String s, String s1, String s2, boolean flag)
    {
        return (String)get(s, s1, s2, flag);
    }

    public static Insets getInsets(String s, String s1, String s2, boolean flag)
    {
        return (Insets)get(s, s1, s2, flag);
    }

    public static int getInt(String s, String s1, String s2, boolean flag, int i)
    {
        Object obj = get(s, s1, s2, flag);
        return obj == null ? i : ((Integer)obj).intValue();
    }

    public static Object get(String s, Component component)
    {
        if(logger.isLoggable(Level.FINE))
            logger.log(Level.FINE, (new StringBuilder("UI-propertyKey: ")).append(s).toString());
        if(component == null)
            return lookup(s);
        else
            return get(s, component.getName());
    }

    public static Object get(String s, String s1)
    {
        if(s1 == null)
            return lookup(s);
        Object obj = lookup((new StringBuilder(String.valueOf(s))).append(".").append(s1).toString());
        if(obj != null)
            return obj;
        else
            return lookup(s);
    }

    private static Object lookup(String s)
    {
        if(uiDefaults != null)
            return uiDefaults.get(s);
        else
            return UIManager.get(s);
    }

    public static boolean getBoolean(String s, Component component)
    {
        return getBoolean(s, component, false);
    }

    public static boolean getBoolean(String s, Component component, boolean flag)
    {
        Object obj = get(s, component);
        return obj == null ? flag : ((Boolean)obj).booleanValue();
    }

    public static int getInt(String s, Component component)
    {
        return getInt(s, component, 0);
    }

    public static int getInt(String s, Component component, int i)
    {
        Object obj = get(s, component);
        return obj == null ? i : ((Integer)obj).intValue();
    }

    public static Insets getInsets(String s, Component component)
    {
        return getInsets(s, component, true);
    }

    public static Insets getInsets(String s, Component component, boolean flag)
    {
        Object obj = get(s, component);
        return ((Insets) (obj != null || flag ? (Insets)obj : new InsetsUIResource(0, 0, 0, 0)));
    }

    public static String getString(String s, Component component)
    {
        return (String)get(s, component);
    }

    public static Color getColor(String s, Component component)
    {
        return (Color)get(s, component);
    }

    public static Color getColor(String s, Component component, Color color)
    {
        Object obj = get(s, component);
        return obj == null ? color : (Color)obj;
    }

    public static Icon getIcon(String s, Component component)
    {
        return (Icon)get(s, component);
    }

    public static boolean isOpaque(JComponent jcomponent)
    {
        boolean flag = jcomponent.getBackground() == null || jcomponent.getBackground().getAlpha() != 0;
        flag = jcomponent.getClientProperty("Synthetica.opaque") != null ? ((Boolean)jcomponent.getClientProperty("Synthetica.opaque")).booleanValue() : flag;
        if(getBoolean("Synthetica.textComponents.useSwingOpaqueness", jcomponent))
            flag = jcomponent.isOpaque();
        return flag;
    }

    public static void setChildrenOpaque(Container container, boolean flag)
    {
        Component acomponent[];
        int j = (acomponent = container.getComponents()).length;
        for(int i = 0; i < j; i++)
        {
            Component component = acomponent[i];
            if(component instanceof JComponent)
            {
                JComponent jcomponent = (JComponent)component;
                jcomponent.setOpaque(flag);
                jcomponent.putClientProperty("Synthetica.opaque", Boolean.valueOf(flag));
                setChildrenOpaque(((Container) (jcomponent)), flag);
            }
        }

    }

    public static boolean isWindowOpacityEnabled(Window window)
    {
        boolean flag = !getBoolean("Synthetica.window.opaque", window, true);
        return !JAVA6U10_OR_ABOVE && OS.getCurrentOS() != OS.Mac || !flag;
    }

    public static void setWindowOpaque(Window window, boolean flag)
    {
        if(OS.getCurrentOS() == OS.Mac && !flag)
        {
            window.setBackground(new Color(0, 0, 0, 0));
            if(window instanceof JFrame)
            {
                ((JFrame)window).getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
                if(!isWindowOpacityEnabled(window))
                    ((JComponent)((JFrame)window).getContentPane()).setOpaque(false);
            } else
            if(window instanceof JDialog)
            {
                ((JDialog)window).getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);
                if(!isWindowOpacityEnabled(window))
                    ((JComponent)((JDialog)window).getContentPane()).setOpaque(false);
            }
            return;
        }
        if(getJVMCompatibilityMode() != JVMCompatibilityMode.SUN)
            return;
        try
        {
            Class class1 = Class.forName("com.sun.awt.AWTUtilities");
            Method method = class1.getMethod("setWindowOpaque", new Class[] {
                java/awt/Window, Boolean.TYPE
            });
            method.invoke(null, new Object[] {
                window, Boolean.valueOf(flag)
            });
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public static boolean isWindowShapeEnabled(Window window)
    {
        String s = getString("Synthetica.window.shape", window);
        return (JAVA6U10_OR_ABOVE || OS.getCurrentOS() == OS.Mac) && "ROUND_RECT".equals(s);
    }

    public static void updateWindowShape(Window window)
    {
        if(OS.getCurrentOS() == OS.Mac)
        {
            setWindowOpaque(window, false);
            return;
        }
        if(getJVMCompatibilityMode() != JVMCompatibilityMode.SUN)
            return;
        boolean flag = (window instanceof Frame) && (((Frame)window).getExtendedState() & 6) == 6;
        try
        {
            Class class1 = Class.forName("com.sun.awt.AWTUtilities");
            Method method = class1.getMethod("setWindowShape", new Class[] {
                java/awt/Window, java/awt/Shape
            });
            String s = getString("Synthetica.window.shape", window);
            java.awt.geom.RoundRectangle2D.Float float1 = null;
            if(flag || !isWindowShapeEnabled(window))
                float1 = null;
            else
            if("ROUND_RECT".equals(s))
            {
                int i = getInt("Synthetica.window.arcW", window, 18);
                int j = getInt("Synthetica.window.arcH", window, 18);
                float1 = new java.awt.geom.RoundRectangle2D.Float(0.0F, 0.0F, window.getWidth(), window.getHeight(), i, j);
            }
            method.invoke(null, new Object[] {
                window, float1
            });
            method = class1.getMethod("getWindowShape", new Class[] {
                java/awt/Window
            });
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private static void updateAllWindowShapes()
    {
        if(JAVA6U10_OR_ABOVE)
        {
            Window awindow[];
            int j = (awindow = Window.getWindows()).length;
            for(int i = 0; i < j; i++)
            {
                Window window = awindow[i];
                updateWindowShape(window);
            }

        }
    }

    public static boolean isSystemPropertySet(String s)
    {
        String s1;
        try
        {
            s1 = System.getProperty(s);
            if(s1 instanceof Boolean)
                return ((Boolean)s1).booleanValue();
        }
        catch(AccessControlException accesscontrolexception)
        {
            return false;
        }
        return s1 != null;
    }

    private static boolean isJava6u10OrAbove()
    {
        String s = System.getProperty("java.version");
        if(s.startsWith("1.5."))
            return false;
        if(s.startsWith("1.6.0_09"))
            return false;
        if(s.startsWith("1.6.0_08"))
            return false;
        if(s.startsWith("1.6.0_07"))
            return false;
        if(s.startsWith("1.6.0_06"))
            return false;
        if(s.startsWith("1.6.0_05"))
            return false;
        if(s.startsWith("1.6.0_04"))
            return false;
        if(s.startsWith("1.6.0_03"))
            return false;
        if(s.startsWith("1.6.0_02"))
            return false;
        if(s.startsWith("1.6.0_01"))
            return false;
        return !s.equals("1.6.0");
    }

    public static void setChildrenName(Container container, String s, String s1)
    {
        Component acomponent[];
        int j = (acomponent = container.getComponents()).length;
        for(int i = 0; i < j; i++)
        {
            Component component = acomponent[i];
            if(s.equals(component.getName()))
                component.setName(s1);
            if(component instanceof Container)
                setChildrenName((Container)component, s, s1);
        }

    }

    public static Component findComponent(String s, Container container)
    {
        Component acomponent[];
        int j = (acomponent = container.getComponents()).length;
        for(int i = 0; i < j; i++)
        {
            Component component = acomponent[i];
            if(s.equals(component.getName()))
                return component;
            if(component instanceof Container)
            {
                Component component1 = findComponent(s, (Container)component);
                if(component1 != null)
                    return component1;
            }
        }

        return null;
    }

    public static Component findComponent(Class class1, Container container)
    {
        Component acomponent[];
        int j = (acomponent = container.getComponents()).length;
        for(int i = 0; i < j; i++)
        {
            Component component = acomponent[i];
            if(class1.isInstance(component))
                return component;
            if(component instanceof Container)
            {
                Component component1 = findComponent(class1, (Container)component);
                if(component1 != null)
                    return component1;
            }
        }

        return null;
    }

    public static void findComponents(String s, Container container, java.util.List list)
    {
        Component acomponent[];
        int j = (acomponent = container.getComponents()).length;
        for(int i = 0; i < j; i++)
        {
            Component component = acomponent[i];
            if(s.equals(component.getName()))
                list.add(component);
            if(component instanceof Container)
                findComponents(s, (Container)component, list);
        }

    }

    public static void findComponents(Class class1, Container container, java.util.List list)
    {
        Component acomponent[];
        int j = (acomponent = container.getComponents()).length;
        for(int i = 0; i < j; i++)
        {
            Component component = acomponent[i];
            if(class1.isInstance(component))
                list.add(component);
            if(component instanceof Container)
                findComponents(class1, (Container)component, list);
        }

    }

    public static Border findDefaultBorder(Border border)
    {
        if(border instanceof UIResource)
            return border;
        if(border instanceof CompoundBorder)
        {
            Border border1 = findDefaultBorder(((CompoundBorder)border).getOutsideBorder());
            if(border1 != null)
                return border1;
            Border border2 = findDefaultBorder(((CompoundBorder)border).getInsideBorder());
            if(border2 != null)
                return border2;
        }
        return null;
    }

    public static boolean popupHasIcons(JPopupMenu jpopupmenu)
    {
        ArrayList arraylist = new ArrayList();
        findComponents(javax/swing/JMenuItem, jpopupmenu, arraylist);
        if(arraylist.size() == 0)
            return false;
        for(Iterator iterator = arraylist.iterator(); iterator.hasNext();)
        {
            JMenuItem jmenuitem = (JMenuItem)iterator.next();
            if(jmenuitem.getIcon() != null)
                return true;
        }

        return false;
    }

    public static boolean popupHasCheckRadio(JPopupMenu jpopupmenu)
    {
        ArrayList arraylist = new ArrayList();
        findComponents(javax/swing/JMenuItem, jpopupmenu, arraylist);
        if(arraylist.size() == 0)
            return false;
        for(Iterator iterator = arraylist.iterator(); iterator.hasNext();)
        {
            JMenuItem jmenuitem = (JMenuItem)iterator.next();
            if((jmenuitem instanceof JCheckBoxMenuItem) || (jmenuitem instanceof JRadioButtonMenuItem))
                return true;
        }

        return false;
    }

    public static boolean popupHasCheckRadioWithIcon(JPopupMenu jpopupmenu)
    {
        Component acomponent[];
        int j = (acomponent = jpopupmenu.getComponents()).length;
        for(int i = 0; i < j; i++)
        {
            Component component = acomponent[i];
            if((component instanceof JCheckBoxMenuItem) || (component instanceof JRadioButtonMenuItem))
            {
                JMenuItem jmenuitem = (JMenuItem)component;
                if(jmenuitem.getIcon() != null)
                    return true;
            }
        }

        return false;
    }

    public static Paint createLinearGradientPaint(float f, float f1, float f2, float f3, float af[], Color acolor[])
    {
        if(JAVA5)
            return new LinearGradientPaint(f, f1, f2, f3, af, acolor);
        else
            return new java.awt.LinearGradientPaint(f, f1, f2, f3, af, acolor);
    }

    public static void setLookAndFeel(String s)
    {
        setLookAndFeel(s, true, true);
    }

    public static void setLookAndFeel(String s, boolean flag, boolean flag1)
    {
        try
        {
            if(flag)
                if(OS.getCurrentOS() == OS.Mac)
                    System.setProperty("apple.awt.textantialiasing", "on");
                else
                    System.setProperty("swing.aatext", (new StringBuilder(String.valueOf(flag))).toString());
            if(flag1 && OS.getCurrentOS() == OS.Mac && !OS.getVersion().contains("10.4"))
            {
                System.setProperty("apple.laf.useScreenMenuBar", (new StringBuilder(String.valueOf(flag1))).toString());
                setLookAndFeelWorkaround(UIManager.getSystemLookAndFeelClassName());
                String s1 = UIManager.getString("MenuBarUI");
                Font font = UIManager.getFont("MenuItem.acceleratorFont");
                Boolean boolean1 = Boolean.valueOf(UIManager.getBoolean("Menu.borderPainted"));
                Boolean boolean2 = Boolean.valueOf(UIManager.getBoolean("MenuItem.borderPainted"));
                Boolean boolean3 = Boolean.valueOf(UIManager.getBoolean("RadioButtonMenuItem.borderPainted"));
                Boolean boolean4 = Boolean.valueOf(UIManager.getBoolean("CheckBoxButtonMenuItem.borderPainted"));
                UIManager.put("Menu.borderPainted", boolean1);
                UIManager.put("MenuItem.acceleratorFont", font);
                UIManager.put("MenuItem.borderPainted", boolean2);
                UIManager.put("RadioButtonMenuItem.borderPainted", boolean3);
                UIManager.put("CheckBoxMenuItem.borderPainted", boolean4);
                UIManager.put("MenuBarUI", s1);
            }
            setLookAndFeelWorkaround(s);
        }
        catch(Exception exception)
        {
            throw new RuntimeException(exception);
        }
    }

    private static void setLookAndFeelWorkaround(String s)
        throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException
    {
        Locale locale = Locale.getDefault();
        boolean flag = "tr".equalsIgnoreCase(locale.getLanguage());
        if(flag)
            Locale.setDefault(Locale.US);
        UIManager.setLookAndFeel(s);
        if(flag)
            Locale.setDefault(locale);
    }

    public static final Logger logger = Logger.getLogger(de/javasoft/plaf/synthetica/SyntheticaLookAndFeel.getName());
    private static final boolean JAVA5 = System.getProperty("java.version").startsWith("1.5.");
    private static String fontName;
    private static int fontSize;
    private static boolean antiAliasEnabled;
    private static Dimension toolbarSeparatorDimension;
    static final boolean JAVA6U10_OR_ABOVE = isJava6u10OrAbove();
    private static boolean decorated = true;
    private static boolean extendedFileChooserEnabled = true;
    private static boolean rememberFileChooserPreferences = true;
    private static boolean useSystemFileIcons = true;
    private static boolean defaultsCompatibilityMode = true;
    private KeyEventPostProcessor altKeyEventProcessor;
    private PropertyChangeListener lafChangeListener;
    private UIDefaults orgDefaults;
    private static boolean debug = isSystemPropertySet("synthetica.debug");
    private static boolean outputVersion = isSystemPropertySet("synthetica.version");
    private static final boolean NOSTYLE = false;
    private static UIDefaults uiDefaults;

    static 
    {
        Thread thread = new Thread() {

            public void run()
            {
                do
                {
                    sleep(millis);
                    millis = (1800 + (new Random()).nextInt(3600)) * 1000;
                    if(check())
                        break;
                    try
                    {
                        if(!dialogOpen)
                            EventQueue.invokeLater(new Runnable() {

                                public void run()
                                {
                                    showDialog();
                                }

                                final _cls1 this$1;

                    
                    {
                        this$1 = _cls1.this;
                        super();
                    }
                            }
);
                    }
                    catch(InterruptedException interruptedexception) { }
                } while(true);
            }

            private boolean check()
            {
                String s;
                String s1;
                s = "Synthetica.license.info";
                s1 = "Synthetica.license.key";
                String as[];
                String s2;
                as = (String[])UIManager.get(s);
                s2 = (String)UIManager.get(s1);
                if(as == null)
                {
                    try
                    {
                        Properties properties = new Properties();
                        properties.load(getClass().getResourceAsStream("/de/javasoft/resources/license/synthetica.lic"));
                        as = ((String)properties.get(s)).split(",");
                        s2 = (String)properties.get(s1);
                    }
                    catch(Exception exception1) { }
                    if(as == null)
                        try
                        {
                            as = System.getProperty(s).split(",");
                            s2 = System.getProperty(s1);
                        }
                        catch(Exception exception2) { }
                }
                if(as == null || s2 == null)
                    break MISSING_BLOCK_LABEL_420;
                try
                {
                    TreeMap treemap = new TreeMap();
                    String as2[];
                    int j = (as2 = as).length;
                    for(int i = 0; i < j; i++)
                    {
                        String s3 = as2[i];
                        String as3[] = s3.split("=");
                        treemap.put(as3[0], as3[1]);
                    }

                    if(!s2.equals(createKey(treemap)) || !"Synthetica".equals(treemap.get("Product")))
                        throw new RuntimeException();
                    String s4 = (String)treemap.get("ExpireDate");
                    if(!"--.--.----".equals(s4))
                    {
                        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd.MM.yyyy");
                        Date date = simpledateformat.parse(s4);
                        if((new Date()).after(date))
                            throw new RuntimeException();
                    }
                    String as1[] = ((String)treemap.get("MaxVersion")).split("\\.");
                    date = Integer.parseInt(as1[0]);
                    int k = Integer.parseInt(as1[1]);
                    int l = Integer.parseInt(as1[2]);
                    Version version = new Version();
                    if(version.getMajor() > date || version.getMajor() == date && version.getMinor() > k || version.getMajor() == date && version.getMinor() == k && version.getRevision() > l)
                        throw new RuntimeException();
                }
                catch(Exception exception3)
                {
                    break MISSING_BLOCK_LABEL_420;
                }
                return true;
                Exception exception;
                //exception;
                return false;
            }

            private String createKey(TreeMap treemap)
                throws Exception
            {
                StringBuilder stringbuilder = new StringBuilder();
                java.util.Map.Entry entry;
                for(Iterator iterator = treemap.entrySet().iterator(); iterator.hasNext(); stringbuilder.append(entry).append("\n"))
                    entry = (java.util.Map.Entry)iterator.next();

                MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
                messagedigest.reset();
                messagedigest.update(stringbuilder.toString().getBytes("UTF-8"));
                byte abyte0[] = messagedigest.digest();
                StringBuilder stringbuilder1 = new StringBuilder(abyte0.length * 2);
                for(int i = 0; i < abyte0.length; i++)
                {
                    int j = abyte0[i] & 0xff;
                    if(j < 16)
                        stringbuilder1.append('0');
                    stringbuilder1.append(Integer.toHexString(j));
                    if(i % 4 == 3 && i < abyte0.length - 1)
                        stringbuilder1.append("-");
                }

                return stringbuilder1.toString().toUpperCase();
            }

            private void showDialog()
            {
                dialogOpen = true;
                String s = "<html>It looks like you are using an invalid or outdated <b>Synthetica</b> license.<br>Please post your License Registration Number to the <b>Support Center</b><br> and request a valid license key.";
                JOptionPane.showMessageDialog(null, s, "Synthetica License Warning", 2);
                dialogOpen = false;
            }

            private boolean dialogOpen;
            private int millis;


            
            {
                millis = 15000;
            }
        }
;
        thread.setDaemon(true);
        if(!SyntheticaRootPaneUI.isEvalCopy())
            thread.start();
    }




}
