public class ContentTypeConvertor {
	private static String[] imgType = {"jpg", "png", "jpeg"};
	private static String[] textType = {"txt", "css", "html", "javascript", "jsom"};
	public static String convertToContentType(String extension) {
		if (isText(extension)) {
			return "text/" + extension;
		}
		if (isImage(extension)) {
			return "image/" + extension;
		}
		return "unknown";
	}

	public static boolean isText(String extension) {
		for (int i = 0; i < textType.length; i++) {
			if (textType[i].equals(extension)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isImage(String extension) {
		for (int i = 0; i < imgType.length; i++) {
			if (imgType[i].equals(extension)) {
				return true;
			}
		}
		return false;
	}

}
