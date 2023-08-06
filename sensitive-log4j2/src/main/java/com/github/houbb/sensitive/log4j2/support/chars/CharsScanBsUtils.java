package com.github.houbb.sensitive.log4j2.support.chars;

import com.github.houbb.chars.scan.api.ICharsReplaceFactory;
import com.github.houbb.chars.scan.api.ICharsReplaceHash;
import com.github.houbb.chars.scan.api.ICharsScanFactory;
import com.github.houbb.chars.scan.bs.CharsScanBs;
import com.github.houbb.chars.scan.support.core.CharsCores;
import com.github.houbb.chars.scan.support.hash.CharsReplaceHashes;
import com.github.houbb.chars.scan.support.replace.CharsReplaces;
import com.github.houbb.chars.scan.support.scan.CharsScans;
import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.trie.api.ITrieTree;
import com.github.houbb.trie.impl.TrieTrees;

import java.util.List;
import java.util.Set;

/**
 * 扫描工具类
 *
 * @since 1.3.0
 */
public final class CharsScanBsUtils {

    /**
     * 创建初始化类
     * @param prefix 前缀
     * @param scanList 扫描列表
     * @param replaceList 替换列表
     * @param defaultReplace 默认替换策略
     * @param replaceHash 替换哈希
     * @param whiteListStr 白名单
     * @return 结果
     */
    public static CharsScanBs buildCharsScanBs(String prefix,
                                               String scanList,
                                               String replaceList,
                                               String defaultReplace,
                                               String replaceHash,
                                               String whiteListStr) {
        // 初始化
        final Set<Character> charsPrefixSet = StringUtil.getCharSet(prefix);
        final ICharsScanFactory charsScanFactory = CharsScans.defaults(StringUtil.splitToList(scanList));
        final ICharsReplaceFactory replaceFactory = CharsReplaces.defaultsReplaceFactory(StringUtil.splitToList(replaceList), defaultReplace);
        final ICharsReplaceHash replaceHashStrategy = CharsReplaceHashes.newInstance(replaceHash);

        ITrieTree trieTree = TrieTrees.node();
        List<String> whiteList = StringUtil.splitToList(whiteListStr);
        trieTree.insert(whiteList);

        // 构建 bs
        return CharsScanBs.newInstance()
                // 核心实现策略
                .charsCore(CharsCores.defaults())
                // 前缀处理策略
                .prefixCharSet(charsPrefixSet)
                // 扫描策略，每一种对应唯一的 scanType
                .charsScanFactory(charsScanFactory)
                // 替换策略
                .charsReplaceFactory(replaceFactory)
                // 替换对应的哈希策略
                .charsReplaceHash(replaceHashStrategy)
                // 添加白名单策略
                .whiteListTrie(trieTree)
                .init();
    }

}
