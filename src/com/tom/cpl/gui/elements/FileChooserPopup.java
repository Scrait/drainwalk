package com.tom.cpl.gui.elements;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

import com.tom.cpl.gui.Frame;
import com.tom.cpl.gui.KeyboardEvent;
import com.tom.cpl.gui.MouseEvent;
import com.tom.cpl.math.Box;
import com.tom.cpl.util.EmbeddedLocalization;
import com.tom.cpm.shared.MinecraftClientAccess;
import com.tom.cpm.shared.util.Log;

public class FileChooserPopup extends PopupPanel {
	private File currDir;
	private File selected;
	private TextField path, name;
	private Runnable accept;
	private Button acceptBtn;
	private boolean saveDialog;
	private FileDisplay files;
	private ScrollPanel filesScroll;
	private BiPredicate<File, String> filter;
	private Function<String, String> extAdder;
	private Label fileDescLabel;
	private EmbeddedLocalization title, desc;
	private Frame frm;

	public FileChooserPopup(Frame frame) {
		super(frame.getGui());
		this.frm = frame;
		setBounds(new Box(0, 0, 310, 220));
		fileDescLabel = new Label(gui, "");
		fileDescLabel.setBounds(new Box(5, 185, 200, 10));
		addElement(fileDescLabel);
		currDir = MinecraftClientAccess.get().getGameDir().getAbsoluteFile();
		if(currDir.getAbsolutePath().endsWith("/.") || currDir.getAbsolutePath().endsWith("\\.")) {
			currDir = currDir.getParentFile();
		}
		path = new TextField(gui);
		name = new TextField(gui);
		path.setBounds(new Box(5, 10, 300, 20));
		name.setBounds(new Box(5, 195, 260, 20));
		addElement(name);
		addElement(path);
		acceptBtn = new Button(gui, "", null);
		acceptBtn.setBounds(new Box(265, 195, 40, 20));
		addElement(acceptBtn);
		path.setText(currDir.getAbsolutePath());
		files = new FileDisplay();
		files.setBackgroundColor(gui.getColors().panel_background);
		files.setBounds(new Box(0, 0, 300, 0));
		filesScroll = new ScrollPanel(gui);
		filesScroll.setDisplay(files);
		filesScroll.setBounds(new Box(5, 35, 300, 135));
		addElement(filesScroll);
		filter = (a, b) -> true;
		extAdder = Function.identity();

		if(gui.getNative().isSupported(FileChooserPopup.class)) {
			Button openNative = new Button(gui, "...", this::openNative);
			openNative.setTooltip(new Tooltip(frm, gui.i18nFormat("tooltip.cpm.filechooser.openNative")));
			openNative.setBounds(new Box(265, 175, 40, 20));
			addElement(openNative);
		}

		acceptBtn.setAction(this::accept);
		path.setEventListener(() -> {
			File dir = new File(path.getText());
			if(dir.exists() && dir.isDirectory()) {
				currDir = dir;
				files.refresh();
			}
		});
		name.setEventListener(() -> {
			selected = new File(currDir, name.getText());
		});
		files.refresh();
	}

	private void accept() {
		if(selected != null) {
			if(saveDialog) {
				if(!filter.test(selected, selected.getName())) {
					File ext = new File(selected.getParentFile(), extAdder.apply(selected.getName()));
					if(filter.test(ext, ext.getName())) {
						selected = ext;
						name.setText(ext.getName());
					}
				}
				if(selected.exists()) {
					ConfirmPopup.confirm(frm, gui.i18nFormat("label.cpm.overwrite"), gui.i18nFormat("label.cpm.overwrite"), () -> {
						close();
						accept.run();
					});
				} else {
					close();
					accept.run();
				}
			} else if(selected.exists()) {
				if(!filter.test(selected, selected.getName()))return;
				close();
				accept.run();
			}
		} else {
			if(!filter.test(currDir, currDir.getName()))return;
			selected = currDir;
			close();
			accept.run();
		}
	}

	public void setTitle(EmbeddedLocalization text) {
		title = text;
	}

	public void setCurrentDirectory(File currDir) {
		this.currDir = currDir;
		selected = new File(currDir, name.getText());
		path.setText(currDir.getAbsolutePath());
		files.refresh();
	}

	public File getCurrentDirectory() {
		return currDir;
	}

	public File getSelected() {
		return selected;
	}

	public void setSelected(File sel) {
		selected = sel;
		if(sel != null) {
			this.currDir = sel.getParentFile();
			name.setText(sel.getName());
			path.setText(currDir.getAbsolutePath());
			files.refresh();
		}
	}

	public void setAccept(Runnable accept) {
		this.accept = accept;
	}

	public void setAccept(Consumer<File> accept) {
		this.accept = () -> accept.accept(selected);
	}

	public void setButtonText(String text) {
		acceptBtn.setText(text);
	}

	public void setSaveDialog(boolean saveDialog) {
		this.saveDialog = saveDialog;
	}

	public void setFilter(BiPredicate<File, String> filter) {
		this.filter = filter;
		files.refresh();
	}

	public void setFileDescText(EmbeddedLocalization text) {
		this.fileDescLabel.setText(text.getTextNormal(gui));
		this.desc = text;
	}

	public void setExtAdder(Function<String, String> extAdder) {
		this.extAdder = extAdder;
	}

	public void setFileName(String name) {
		this.name.setText(name);
		selected = new File(currDir, name);
	}

	private class FileDisplay extends Panel {
		private String[] files;

		public FileDisplay() {
			super(FileChooserPopup.this.getGui());
		}

		@Override
		public void draw(MouseEvent event, float partialTicks) {
			gui.pushMatrix();
			gui.setPosOffset(bounds);
			event = event.offset(bounds);
			int y = 0;
			for (int i = 0; i < files.length; i++) {
				String string = files[i];
				int yp = y++;
				int textColor = 0xffffffff;
				if (event.isHovered(new Box(0, yp * 10, bounds.w, 10))) {
					textColor = 0xffffff00;
				}
				if(selected != null && selected.getName().equals(string) && selected.getParentFile().equals(currDir)) {
					gui.drawBox(0, yp * 10, bounds.w, 10, 0xff6666ff);
				}
				gui.drawText(2, yp * 10 + 1, string, textColor);
			}
			gui.popMatrix();
		}

		@Override
		public void mouseClick(MouseEvent event) {
			if(event.isInBounds(bounds) && !event.isConsumed()) {
				int yp = (event.y - bounds.y) / 10;
				if(yp >= 0 && yp < files.length) {
					String string = files[yp];
					if(selected != null && selected.getName().equals(string) && selected.getParentFile().equals(currDir)) {
						if(selected.isDirectory()) {
							try {
								currDir = selected.getCanonicalFile();
							} catch (IOException e) {
								e.printStackTrace();
							}
							selected = null;
							refresh();
							event.consume();
							return;
						}
					}
					selected = new File(currDir, string);
					if(filter.test(selected, string))
						name.setText(selected.getName());
				}
				event.consume();
			}
		}

		private void refresh() {
			String[] fs = currDir.list((a, name) -> {
				File file = new File(currDir, name);
				if(file.isHidden())return false;
				if(file.isDirectory())return true;
				return filter.test(file, name);
			});
			if(currDir.toPath().getNameCount() == 0) {
				files = fs;
			} else {
				files = new String[fs.length + 1];
				files[0] = "..";
				System.arraycopy(fs, 0, files, 1, fs.length);
			}
			path.setText(currDir.getAbsolutePath());
			setBounds(new Box(bounds.x, bounds.y, bounds.w, files.length * 10 + 2));
			filesScroll.setScrollY(0);
		}
	}

	@Override
	public String getTitle() {
		return title.getTextNormal(gui);
	}

	public String getSafeTitle() {
		return title.getTextSafe(gui);
	}

	public String getDescription() {
		return desc.getTextNormal(gui);
	}

	public String getSafeDescription() {
		return desc.getTextSafe(gui);
	}

	public BiPredicate<File, String> getFilter() {
		return filter;
	}

	public static class FileFilter implements BiPredicate<File, String> {
		private boolean folder;
		private String ext;
		public FileFilter(String ext) {
			this.ext = ext;
		}

		public FileFilter(boolean allowFolder) {
			this.folder = allowFolder;
		}

		@Override
		public boolean test(File f, String n) {
			if(ext != null && !n.endsWith("." + ext))return false;
			if(folder != f.isDirectory())return false;
			return true;
		}

		public String getExt() {
			return ext;
		}

		public boolean isFolder() {
			return folder;
		}
	}

	public boolean isSaveDialog() {
		return saveDialog;
	}

	public void openNative() {
		NativeChooser nc = gui.getNative().getNative(FileChooserPopup.class, this);
		CompletableFuture<File> f = nc.openFuture();
		if(f == null) {
			f = new CompletableFuture<>();
			new ProcessPopup<>(frm, gui.i18nFormat("label.cpm.waiting"), gui.i18nFormat("label.cpm.filechooser.waitingForNative"), nc::open, f).start();
		}
		f.thenAcceptAsync(this::setSelected, gui::executeLater).exceptionally(ex -> {
			if(ex instanceof InterruptedException)return null;
			Log.error("Error while opening native file chooser", ex);
			frm.openPopup(new MessagePopup(frm, gui.i18nFormat("label.cpm.error"), gui.i18nFormat("label.cpm.filechooser.nativeError", ex.getMessage())));
			return null;
		});
	}

	public interface NativeChooser {
		File open();

		default CompletableFuture<File> openFuture() {
			return null;
		}
	}

	@Override
	public void keyPressed(KeyboardEvent event) {
		if(event.matches(gui.getKeyCodes().KEY_ENTER) || event.matches(gui.getKeyCodes().KEY_KP_ENTER)) {
			accept();
			event.consume();
		}
		super.keyPressed(event);
	}

	public Frame getFrame() {
		return frm;
	}

	@Override
	public void filesDropped(List<File> files) {
		if(!saveDialog && files.size() == 1) {
			File f = files.get(0);
			if(filter.test(f, f.getName())) {
				setSelected(f);
			}
		}
	}
}
