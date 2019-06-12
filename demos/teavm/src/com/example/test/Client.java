package com.example.test;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.jso.canvas.CanvasRenderingContext2D;
import org.teavm.jso.dom.events.Event;
import org.teavm.jso.dom.events.EventListener;
import org.teavm.jso.dom.html.HTMLButtonElement;
import org.teavm.jso.dom.html.HTMLCanvasElement;
import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.html.TextRectangle;
import org.teavm.jso.json.JSON;

import com.hocuscodus.qed.lang.Call;
import com.hocuscodus.qed.lang.Event.MouseEvent;
import com.hocuscodus.qed.lang.Obj;
import com.hocuscodus.qed.lang.ProcessReturnHandler;
import com.hocuscodus.qed.lang.QEDProcess;
import com.hocuscodus.qed.node.ContentNode;
import com.hocuscodus.qed.node.Node;

@SuppressWarnings("serial")
public class Client extends QEDProcess {
//    private static HTMLDocument document = HTMLDocument.current();
    private static HTMLCanvasElement canvas;
    private static HTMLInputElement code;
    private static CanvasRenderingContext2D graphics;
    private static Client process;
    private int width;
    private int height;
    private int color;
    private Timer timer = new Timer();

    private Client(int width, int height) {
        this.width = width;
        this.height = height;
    }

	public Reader getReader(String fileName) {
		return new StringReader(code.getValue());
	}

	public void init() {
        String[] fontArgs = graphics.getFont().split(" ");

		graphics.setFont(14 + "px " + fontArgs[fontArgs.length - 1]);
	}

	public void init2(int[] totalSize) {
//		graphics.setGlobalAlpha(1); 
//		graphics.setFillStyle("#ffffff");
//		graphics.fillRect(0, 0, width, height);
////		canvas.setWidth(1);
////		canvas.setWidth(800);
//		graphics.clearRect(0, 0, width, height);
//		graphics.beginPath();
//		graphics.save();
//		graphics.setGlobalCompositeOperation("copy");
//		graphics.setStrokeStyle("transparent");
//		graphics.beginPath();
//		graphics.lineTo(0, 0);
//		graphics.stroke();
//		graphics.restore();
		canvas.setWidth(totalSize[0]);//Math.max(totalSize[0], 150));
		canvas.setHeight(totalSize[1]);//Math.max(totalSize[1], 100));
        String[] fontArgs = graphics.getFont().split(" ");

		graphics.setFont(14 + "px " + fontArgs[fontArgs.length - 1]);
	}

	public void uninit() {
		Node.log3("Refresh called on windows");
		repaintNow();
	}

	@Override
	public int getavgcharwidth(int fontSize) {
		int oldSize = getFontSize();

		return (8 * (fontSize != -1 ? fontSize : oldSize) / oldSize);
	}

	@Override
	public int getavgcharheight(int fontSize) {
		int currentSize = getFontSize();
		String font = setFontSize(fontSize);
		int oldSize = fontSize != -1 ? fontSize : currentSize;

		int size = 16 * oldSize / currentSize;

		resetFontSize(font);
		return size;// * 15 / 10;
	}

	@Override
	public int gettextsize(String value, int fontSize) {
		String font = setFontSize(fontSize);
		int size = graphics.measureText(value).getWidth();//getter.gettextsize(value, fontSize);

		resetFontSize(font);
		return size;
	}

	@Override
	public int gettextheight(String value, int fontSize) {
//		int size = graphics.getFontMetrics().getMaxAscent() + graphics.getFontMetrics().getMaxDescent()/* + 3*/;
		return (fontSize != -1 ? fontSize : getFontSize()) * 15 / 10;//getter.gettextheight(value, fontSize);
	}

	@Override
	public int[] getscreenarea() {
		return new int[] {800, 480};//getter.getscreenarea();
	}

	@Override
	public boolean resize(int[] size) {
		return true;//getter.resize(size);
	}

	@Override
	public boolean refresh(int[] pos, int[] size) {
		return true;//getter.refresh(pos, size);
	}

	@Override
	public void repaintNow() {
	}

	@Override
	public Object createImageBuffer() {
        return graphics.createImageData(width, height);
	}

	@Override
	public Object loadImage(String name, QEDProcess process, Obj obj) {
		final HTMLImageElement element = (HTMLImageElement) HTMLDocument.current().createElement("img");

		element.addEventListener("load", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				obj.onReturn(process, element);
				process.tick();
			}
		});
		element.addEventListener("error", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				obj.onReturn(process, null);
				process.tick();
			}
		});
		element.setSrc(name);
		return null;
	}

	public void saveContext() {
		graphics.save();
	}

	public void restoreContext() {
		graphics.restore();
	}

	public int getImageWidth(Object image, int fontSize) {
		return ((HTMLImageElement) image).getWidth();
	}

	public int getImageHeight(Object image, int fontSize) {
		return ((HTMLImageElement) image).getHeight();
	}

	@Override
	public Object setTimer(long delay, final QEDProcess process, final Obj obj) {
		TimerTask timerTask = new TimerTask() {
	        public void run() {
	        	try {
	        		obj.onReturn(process, null);
	        		process.tick();
	        	}
	        	catch(Exception e) {
	        		e.printStackTrace();
	        	}
	        }
	    };
	    timer.schedule(timerTask, delay);

	    return timerTask;
	}

	@Override
	public void resetTimer(Object timerObjs) {
		final Object[] objs = (Object[]) timerObjs;
		TimerTask timerTask = (TimerTask) objs[0];
		Integer delay = (Integer) objs[3];

		timerTask.cancel();

		if (delay >= 0) {
			objs[0] = timerTask = new TimerTask() {
		        public void run() {
		        	try {
			    		Obj obj = (Obj) objs[1];
			    		QEDProcess process = (QEDProcess) objs[2];

			    		obj.onReturn(process, null);
						process.tick();
		        	}
		        	catch(Exception e) {
		        		e.printStackTrace();
		        	}
		        }
		    };

			timer.schedule(timerTask, delay);
		}
		else
			objs[0] = null;
	}

	public void setclippingarea(int[] pos, int[] size) {
		graphics.beginPath();
		graphics.moveTo(pos[0], pos[1]);
		graphics.lineTo(pos[0], pos[1] + size[1]);
		graphics.lineTo(pos[0] + size[0], pos[1] + size[1]);
		graphics.lineTo(pos[0] + size[0], pos[1]);
		graphics.lineTo(pos[0], pos[1]);
		graphics.clip();
	}

	public void setColor(int col) {
		color = col;

		if (col != -1) {
			int alpha = 0xFF ^ ((col >>> 24) & 0xFF);

			graphics.setGlobalAlpha(((double) alpha) / 255); 
			graphics.setFillStyle("rgb(" + ((col >>> 16) & 0xFF) + ", " + ((col >>> 8) & 0xFF) + ", " + (col & 0xFF) + ")");
		}
	}

	public void fillRectangle(int[] pos, int[] size) {
		graphics.fillRect(pos[0], pos[1], size[0], size[1]);
	}

	public void fillRoundRectangle(int[] pos, int[] size, int[] arc) {
		arc[0] = arc[0] * 8 / 10;
		arc[1] = arc[1] * 8 / 10;
		graphics.beginPath();
		graphics.moveTo(pos[0], pos[1] + arc[1]);
		graphics.lineTo(pos[0], pos[1] + size[1] - arc[1]);
		graphics.arcTo(pos[0], pos[1] + size[1], pos[0] + arc[0], pos[1] + size[1], arc[1]);
		graphics.lineTo(pos[0] + size[0] - arc[0], pos[1] + size[1]);
		graphics.arcTo(pos[0] + size[0], pos[1] + size[1], pos[0] + size[0], pos[1] + size[1] - arc[1], arc[1]);
		graphics.lineTo(pos[0] + size[0], pos[1] + arc[1]);
		graphics.arcTo(pos[0] + size[0], pos[1], pos[0] + size[0] - arc[0], pos[1], arc[1]);
		graphics.lineTo(pos[0] + arc[0], pos[1]);
		graphics.arcTo(pos[0], pos[1], pos[0], pos[1] + arc[1], arc[1]);
		graphics.fill();
		graphics.clip();
	}

	public void fillOval(int[] pos, int[] size) {
		int rx = size[0] / 2;
		int ry = size[1] / 2;
		float kappa = 0.5522848F;
	    float ox = (size[0] / 2) * kappa; // control point offset horizontal
	    float oy = (size[1] / 2) * kappa; // control point offset vertical
	    float xe = pos[0] + size[0];           // x-end
	    float ye = pos[1] + size[1];           // y-end
	    float xm = pos[0] + rx;       // x-middle
	    float ym = pos[1] + ry;       // y-middle

		graphics.beginPath();
		graphics.moveTo(pos[0], ym);
		graphics.bezierCurveTo(pos[0], ym - oy, xm - ox, pos[1], xm, pos[1]);
		graphics.bezierCurveTo(xm + ox, pos[1], xe, ym - oy, xe, ym);
		graphics.bezierCurveTo(xe, ym + oy, xm + ox, ye, xm, ye);
		graphics.bezierCurveTo(xm - ox, ye, pos[0], ym + oy, pos[0], ym);
		//ctx.closePath(); // not used correctly, see comments (use to close off open path)
		graphics.fill();
		graphics.clip();
	}

	public void line(int[] pos1, int[] pos2) {
		graphics.moveTo(pos1[0], pos1[1]);
		graphics.lineTo(pos2[0], pos2[1]);
		graphics.stroke();
	}

	public void line(int[] pos1, int[] pos2, int stroke) {
		graphics.moveTo(pos1[0], pos1[1]);
		graphics.lineTo(pos2[0], pos2[1]);
		graphics.stroke();
//		Stroke oldStroke = graphics.getStroke();
//
//		graphics.stro(new BasicStroke(stroke));
//		graphics.drawLine(pos1[0], pos1[1], pos2[0], pos2[1]);
//		graphics.setStroke(oldStroke);
	}

	public int getFontSize() {
//		String font = graphics.getFont();
//		String[] fontArgs = font.split(" ");
//		HTMLDocument htmlDoc = HTMLDocument.current();
//		final HTMLBodyElement body = (HTMLBodyElement)htmlDoc.getBody();
//
//		//JS [window] access
//		DOMWindowImpl wnd =
//		    (DOMWindowImpl)((DocumentView)htmlDoc).getDefaultView();
//
//		//Style access
//		CSSStyleDeclaration style = wnd.getComputedStyle(body, "");
//
//		String style = Window.getComputedStyle(canvas, null).getPropertyValue("font-size");
//		var fontSize = parseFloat(style); 
//
//			graphics.setFont(fontSize + "px " + fontArgs[0]);
//		}
//
//		String font = graphics.getFont();
//		String[] fontArgs = font.split(" ");
//
//		graphics.setFont(fontArgs[0]);
		return 14;
	}

	public String setFontSize(int fontSize) {
		String font = fontSize != -1 ? graphics.getFont() : null;

		if (font != null) {
			String[] fontArgs = font.split(" ");

			graphics.setFont(fontSize + "px " + fontArgs[fontArgs.length - 1]);
		}

		return font;
	}

	public void resetFontSize(String font) {
		if (font != null)
			graphics.setFont(font);
	}

	public void drawText(String string, int[] pos, int[] size, int[] unitpos, int fontSize, int textcol) {
		String font = setFontSize(fontSize);

		if (color != -1)
			graphics.fillRect(pos[0], pos[1], size[0], size[1]);

		setColor(textcol != -1 ? textcol : 0);
		graphics.fillText(string, pos[0], pos[1] + (fontSize != -1 ? fontSize : getFontSize())/*graphics.getTextBaseline()*/);
		resetFontSize(font);
//		Font font = graphics.getFont();
//		int oldSize = fontSize != -1 ? fontSize : font.getSize();
//
//		if (fontSize != -1)
//			graphics.setFont(new Font(font.getFontName(), font.getStyle(), fontSize));
//		//				graphics.setFont(new Font("IM FELL DW Pica PRO"/*font.getFontName()*/, font.getStyle(), 20/*fontSize*/));
//		int ascent = graphics.getFontMetrics().getAscent();
//
//		graphics.drawString(string, pos[0], pos[1] + oldSize * ascent / oldSize);
//
//		if (fontSize != -1)
//			graphics.setFont(font);
	}

	public void drawImage(Object image, int[] pos, int[] unitpos) {
		HTMLImageElement trueImage = (HTMLImageElement) image;

		saveContext();
		setclippingarea(pos, new int[] {trueImage.getWidth(), trueImage.getHeight()});
		graphics.drawImage(trueImage, unitpos[0], unitpos[1]);
		restoreContext();
	}

	public void print(String string) {
        HTMLElement lineElem = (HTMLElement) stdoutElement.getLastChild();
        lineElem.withText(lineElem.getInnerHTML() + string);

//        stdoutElement.setScrollTop(Math.max(0, stdoutElement.getScrollHeight() - stdoutElement.getClientHeight()));
	}

	public void println(String string) {
		print(string);
        HTMLElement lineElem = (HTMLElement) stdoutElement.getLastChild();
        lineElem.withText(lineElem.getInnerHTML());

        stdoutElement.appendChild(HTMLDocument.current().createElement("div").withText(""));
//        stdoutElement.setScrollTop(Math.max(0, stdoutElement.getScrollHeight() - stdoutElement.getClientHeight()));
	}

	public void exit(int value) {
		graphics.clearRect(0, 0, width, height);
		process = null;
	}

public interface FrameCommand extends JSObject {
    @JSProperty
    String getCommand();

    @JSProperty
    void setCommand(String command);
}
public interface FrameStdoutCommand extends FrameCommand {
    @JSProperty
    String getLine();
}
	static HTMLElement stdoutElement;
//    private static void initStdout() {
//        Window.current().addEventListener("message", (MessageEvent event) -> {
//            FrameCommand request = JSON.parse(((JSString) event.getData()).stringValue()).cast();
//            if (request.getCommand().equals("stdout")) {
//                FrameStdoutCommand stdoutCommand = request.cast();
//                addToConsole(stdoutCommand.getLine(), false);
//            }
//        });
//    }
//
	public static void clearStdOut() {
		stdoutElement.clear();
        stdoutElement.appendChild(HTMLDocument.current().createElement("div").withText(""));
//        stdoutElement.setScrollTop(Math.max(0, stdoutElement.getScrollHeight() - stdoutElement.getClientHeight()));
	}

    private static void addToConsole(String line, boolean compileTime) {
        HTMLElement lineElem = HTMLDocument.current().createElement("div").withText(line);
        if (compileTime) {
            lineElem.setClassName("compile-time");
        }
        stdoutElement.appendChild(lineElem);
//        stdoutElement.setScrollTop(Math.max(0, stdoutElement.getScrollHeight() - stdoutElement.getClientHeight()));
    }

    static class ExampleCategory {
        String title;
        Map<String, String> items = new LinkedHashMap<>();
    }

    public static interface JsonObject extends JSObject {
    	@JSIndexer
    	JsonObject get(String key);

    	@JSIndexer
    	String getAsString(String key);
    }

    private static HTMLButtonElement examplesButton;
    private static HTMLElement examplesDialog;
    private static HTMLElement examplesBackdrop;
    private static String examplesBaseUrl = "examples/";
    private static final Map<String, ExampleCategory> categories = new HashMap<>();

    private static String getParm(String[] parms, String name) {
    	for (int index = 0; index < parms.length; index++)
    		if (parms[index].startsWith(name + "="))
    			return parms[index].substring(name.length() + 1);

    	return null;
    }

    private static void initExamples() {
        HTMLDocument document = HTMLDocument.current();
        String url = getURL(document);

        examplesButton.listenClick(event -> showExamples());

        HTMLButtonElement cancelButton = document.getElementById("cancel-example-selection").cast();
        cancelButton.listenClick(event -> hideExamples());

        XMLHttpRequest request = XMLHttpRequest.create();
        request.open("get", examplesBaseUrl + "examples.json");
        request.onComplete(() -> {
            loadExamples(JSON.parse(request.getResponseText()).cast());
            renderExamples();
            examplesButton.setDisabled(false);
            int parmIndex = url.indexOf('?');

            if (parmIndex != -1) {
            	String parmString = url.substring(parmIndex + 1);
            	String[] parms = parmString.split("&");
            	String category = getParm(parms, "category");
            	String name = getParm(parms, "name");

            	if (category != null && name != null)
            		loadExample(category, name);
            }
        });
        request.send();
    }

    private static void loadExamples(JsonObject object) {
        for (String key : keys(object)) {
            ExampleCategory category = new ExampleCategory();
            JsonObject categoryObject = object.get(key);
            category.title = categoryObject.getAsString("title");
            categories.put(key, category);

            JsonObject categoryItems = categoryObject.get("items");
            for (String itemKey : keys(categoryItems)) {
                String itemTitle = categoryItems.getAsString(itemKey);
                category.items.put(itemKey, itemTitle);
            }
        }
    }

    private static void renderExamples() {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement container = document.getElementById("examples-content");
        for (Map.Entry<String, ExampleCategory> categoryEntry : categories.entrySet()) {
            ExampleCategory category = categoryEntry.getValue();
            container.appendChild(document.createElement("h3").withText(category.title));
            for (Map.Entry<String, String> entry : category.items.entrySet()) {
                HTMLElement itemElement = document.createElement("div");
                itemElement.appendChild(document.createElement("span").withText(entry.getValue()));
                itemElement.setClassName("example-item");
                itemElement.listenClick(event -> loadPage(categoryEntry.getKey(), entry.getKey()));
                container.appendChild(itemElement);
            }
        }
    }

    private static void loadPage(String category, String item) {
    	String url = getURL(HTMLDocument.current());
        int parmIndex = url.indexOf('?');

        if (parmIndex != -1)
        	url = url.substring(0, parmIndex);

        url += "?category=" + category;
    	url += "&name=" + item;
    	goToURL(url);
    }

    private static void loadExample(String category, String item) {
        HTMLDocument document = HTMLDocument.current();
        HTMLElement progressElement = document.getElementById("examples-content-progress");
        progressElement.getStyle().setProperty("display", "block");

        XMLHttpRequest xhr = XMLHttpRequest.create();
        xhr.open("get", examplesBaseUrl + "/" + category + "/" + item + ".qed");
        xhr.onComplete(() -> {
            String code = xhr.getResponseText();
            Client.code.setValue(code);
            hideExamples();
            progressElement.getStyle().setProperty("display", "none");
    		canvas.setWidth(canvas.getWidth());
        });
        xhr.send();
    }

    @JSBody(params = "o", script = "return Object.keys(o);")
    private static native String[] keys(JSObject o);

    @JSBody(params = "document", script = "return document.URL;")
    private static native String getURL(HTMLDocument document);

    @JSBody(params = "url", script = "window.location.href = url;")
    private static native void goToURL(String url);

    private static void showExamples() {
        HTMLDocument document = HTMLDocument.current();
        examplesDialog.getStyle().setProperty("display", "block");
        examplesDialog.setClassName("modal fade in");
        examplesBackdrop = document.createElement("div").withAttr("class", "modal-backdrop fade in");
        document.getBody().appendChild(examplesBackdrop);
    }

    private static void hideExamples() {
        examplesDialog.getStyle().setProperty("display", "none");
        examplesDialog.setClassName("modal fade");
        examplesBackdrop.delete();
        examplesBackdrop = null;
    }

	public static void main(final String argv[]) {
	    examplesButton = HTMLDocument.current().getElementById("select").cast();
	    examplesDialog = HTMLDocument.current().getElementById("examples");
        canvas = HTMLDocument.current().getElementById("canvas").cast();
        graphics = canvas.getContext("2d").cast();
        code = HTMLDocument.current().getElementById("code").cast();
        stdoutElement = HTMLDocument.current().getElementById("stdout");
        initExamples();

        EventListener mouseEventListener = new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				if (process != null) {
					org.teavm.jso.dom.events.MouseEvent mouseEvent = evt.cast();
					TextRectangle canvasRect = canvas.getBoundingClientRect();
					int[] mousepos = new int[] {mouseEvent.getClientX() - canvasRect.getLeft(),
												mouseEvent.getClientY() - canvasRect.getTop()};
	
	//				this.mousepos = mousepos;
	
					//		if (form.findfocus(mousepos, ContentNode.MODIFonpress)) {
					//    		Storage.log3("Focus for [" + mousepos[0] + ", " + mousepos[1] + "]: " + form.focus.msg);
					//        	processevent(event);
					//    	}
					process.processEvent(mousepos, evt.getType().equals("mousedown") ? ContentNode.MODIFonpress : ContentNode.MODIFonrelease, new MouseEvent(mousepos));
				}
			}
        };

        canvas.addEventListener("mousedown", mouseEventListener);
        canvas.addEventListener("mouseup", mouseEventListener);

        HTMLDocument.current().getElementById("run").addEventListener("click", new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				if (process != null)
					process.removeAllElements();

				graphics.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
				clearStdOut();
				process = new Client(0, 0);

				try {
					process.run(new String[] {"test"}, new ProcessReturnHandler() {
						public void onPause(QEDProcess process, Call call) {
//							process.println("Paused...");
						}

						public void onHalt(QEDProcess process, Call call) {
						}

						public void onReturn(QEDProcess process, Call call, Object value) {
							super.onReturn(process, call, value);
						}
					});
				} catch(Exception e) {
				}
			}
        });
	}
}
