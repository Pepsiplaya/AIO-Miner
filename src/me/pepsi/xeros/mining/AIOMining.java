package me.pepsi.xeros.mining;

import simple.api.script.Category;
import simple.api.script.ScriptManifest;
import simple.api.script.interfaces.SimplePaintable;
import simple.api.wrappers.SimpleGameObject;
import simple.api.actions.SimpleObjectActions;
import simple.api.coords.WorldArea;
import simple.api.coords.WorldPoint;
import simple.api.events.ChatMessageEvent;
import simple.api.filters.SimpleSkills;
import simple.api.listeners.SimpleMessageListener;
import simple.api.script.Script;

import java.awt.*;

import javax.swing.SwingUtilities;


@ScriptManifest(author = "Pepsiplaya", name = "AIO Mining", category = Category.MINING, version = "1.2D",
        description = "Mining On Skilling Island Till 99. Make sure to have a pickaxe equipped", discord = "Pepsiplaya#6988", servers = { "Xeros" })

public class AIOMining extends Script implements SimplePaintable, SimpleMessageListener {
	
    public String status;
    public boolean started;
    private long startExp;
    public long startTime;
    public int[] oreId;
    private long lastAnimation = -1;
    
    public static final WorldArea HOME_AREA = new WorldArea(
            new WorldPoint(3072, 3521, 0), new WorldPoint(3072, 3464, 0),
            new WorldPoint(3137, 3474, 0), new WorldPoint(3137, 3521, 0));
    
    public static final WorldArea SKILLING_AREA = new WorldArea(
            new WorldPoint(3700, 3400, 0),
            new WorldPoint(3900, 3600, 0));
    
    public GUI gui;
    
    @Override
    public boolean onExecute() {
    	ctx.skills.getRealLevel(SimpleSkills.Skill.MINING);
    	startExp = ctx.skills.getExperience(SimpleSkills.Skill.MINING);
        startTime = System.currentTimeMillis();
		status = "READ INSTRUCTIONS!";
        try {
            AIOMining script = this;
            SwingUtilities.invokeLater(new Runnable() { @Override public void run() {
                gui = new GUI(script);
            }});

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    
    void bank() {
		SimpleGameObject bank = (SimpleGameObject) ctx.objects.populate().filter(20325).nearest().next();
		SimpleGameObject ore = (SimpleGameObject) ctx.objects.populate().filter(oreId).nearest().next();
		
		if (bank != null) {
			bank.interact(SimpleObjectActions.FIRST);
			status = "Banking";
			ctx.onCondition(() -> ctx.bank.bankOpen(), 10000);
			ctx.bank.depositAllExcept("pickaxe");
			ctx.sleep(1000);
		}
		
		if (ore != null) { 
			status = "Mining";
			ore.interact("Mine");
			ctx.onCondition(() -> !ctx.players.getLocal().isAnimating(), 250, 10);
		}
	}

    @Override
    public void onProcess() {
    	
    	if (!SKILLING_AREA.containsPoint(ctx.players.getLocal())) {
    		return;
        }
    	
    	if (ctx.players.getLocal().isAnimating()) {
    		lastAnimation = System.currentTimeMillis();
    	}

    	if (SKILLING_AREA.containsPoint(ctx.players.getLocal())) {
            if (ctx.inventory.inventoryFull()) {
            	bank();
            }
            SimpleGameObject ore = (SimpleGameObject) ctx.objects.populate().filter(oreId).nearest().next();
            if (!ctx.players.getLocal().isAnimating() && !ctx.inventory.inventoryFull() && System.currentTimeMillis() > (lastAnimation + 3000)) {
            	if (ore != null) { 
            		status = "Mining";
            		ore.interact("Mine");
            		ctx.onCondition(() -> !ctx.players.getLocal().isAnimating(), 250, 10);
                }
            }
        }
    }

    @Override
    public void onTerminate() {
        if (gui != null) {
            gui.onCloseGUI();
        }
    }
    
	public final String format(final long t) {
	    long s = t;
	    return String.format("%", s);
	}
    
	public final String formatValue(final long l) {
	    return (l > 1_000_000) ? String.format("%.2fm", ((double) l / 1_000_000))
	           : (l > 1000) ? String.format("%.1fk", ((double) l / 1000)) 
	           : l + "";
	}
	
    private final Color color1 = new Color(255, 255, 255);
    private final Font font1 = new Font("Arial", 1, 10);
    private final Color color2 = new Color(29, 3, 3, 94);
    private final Color color3 = new Color(0, 0, 0);
    private final BasicStroke stroke1 = new BasicStroke(1);
    
	@Override
    public void onPaint(Graphics2D g) {
		
		final long miningExp = ctx.skills.getExperience(SimpleSkills.Skill.MINING) - startExp;
		
		 g.setColor(color2);
	     g.fillRect(7, 273, 155, 47);
	     g.setColor(color3);
	     g.setStroke(stroke1);
	     g.drawRect(7, 273, 155, 47);
	     g.setFont(font1);
	     g.setColor(color1);
	     g.drawString("Time Running: " + ctx.paint.formatTime(System.currentTimeMillis() - startTime), 10, 287);
	     g.drawString("Exp / Per Hour: " + formatValue(miningExp) + " / " + formatValue(ctx.paint.valuePerHour((int) miningExp, startTime)), 10, 300);
	     g.drawString("Status: " + status, 10, 313);
    }
	
    public void onChatMessage(ChatMessageEvent event) {
    	
    }

    public void setupOres(int[] oreId) {
        this.oreId = oreId;
    }
}