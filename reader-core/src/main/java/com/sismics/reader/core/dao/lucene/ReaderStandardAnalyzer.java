package com.sismics.reader.core.dao.lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.analysis.standard.ClassicTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.StopwordAnalyzerBase;
import org.apache.lucene.util.Version;

/**
 * Filters {@link StandardTokenizer} with {@link StandardFilter}, {@link
 * LowerCaseFilter} and {@link StopFilter}, using a list of
 * English stop words.
 */
public final class ReaderStandardAnalyzer extends StopwordAnalyzerBase {

  /** Default maximum allowed token length */
  private static final int DEFAULT_MAX_TOKEN_LENGTH = 255;

  private int maxTokenLength = DEFAULT_MAX_TOKEN_LENGTH;

  /** An unmodifiable set containing some common English words that are usually not
  useful for searching. */
  private static final CharArraySet STOP_WORDS_SET = StopAnalyzer.ENGLISH_STOP_WORDS_SET; 

  /** Builds an analyzer with the given stop words.
   * @param matchVersion Lucene version to match
   * @param stopWords stop words */
  public ReaderStandardAnalyzer(Version matchVersion, CharArraySet stopWords) {
    super(matchVersion, stopWords);
  }

  /** Builds an analyzer with the default stop words.
   * @param matchVersion Lucene version to match
   */
  public ReaderStandardAnalyzer(Version matchVersion) {
    this(matchVersion, STOP_WORDS_SET);
  }

  /** Builds an analyzer with the stop words from the given reader.
   * @param matchVersion Lucene version to match
   * @param stopwords Reader to read stop words from */
  public ReaderStandardAnalyzer(Version matchVersion, Reader stopwords) throws IOException {
    this(matchVersion, loadStopwordSet(stopwords, matchVersion));
  }

  /**
   * Set maximum allowed token length. If a token is seen
   * that exceeds this length then it is discarded.
   * @param length The maximum allowed token length
   * @throws IllegalArgumentException if length is not positive
   */
  public void setMaxTokenLength(int length) {
    if (length <= 0) {
        throw new IllegalArgumentException("maxTokenLength must be greater than zero");
    }
    maxTokenLength = length;
  }
    
  /**
   * Returns the maximum allowed token length
   * @return the maximum allowed token length
   */
  public int getMaxTokenLength() {
    return maxTokenLength;
  }

  /**
   * Returns a defensive copy of the stop words set
   * @return copy of stop words set
   */
  public CharArraySet getStopWordsSet() {
    return new CharArraySet(matchVersion, STOP_WORDS_SET, true);
  }

  @Override
  protected TokenStreamComponents createComponents(final String fieldName, final Reader reader) {
    final StandardTokenizer src = new StandardTokenizer(matchVersion, reader);
    src.setMaxTokenLength(maxTokenLength);
    TokenStream tok = new StandardFilter(matchVersion, src);
    tok = new LowerCaseFilter(matchVersion, tok);
    tok = new StopFilter(matchVersion, tok, stopwords);
    return new TokenStreamComponents(src, tok) {
      @Override
      protected void setReader(final Reader reader) throws IOException {
        src.setMaxTokenLength(ReaderStandardAnalyzer.this.maxTokenLength);
        super.setReader(reader);
      }
    };
  }
  
  @Override
  protected Reader initReader(String fieldName, Reader reader) {
    if (fieldName.equals("title") || fieldName.equals("description")) {
        return new HTMLStripCharFilter(super.initReader(fieldName, reader));
    }
    return super.initReader(fieldName, reader);
  }
}