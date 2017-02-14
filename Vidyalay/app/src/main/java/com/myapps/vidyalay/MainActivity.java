package com.myapps.vidyalay;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Adding menu icon to Toolbar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setTitle("  Vidyalay  |  विद्यालय ");

        int listImages[] = new int[]{R.drawable.education, R.drawable.agriculture,
                R.drawable.english, R.drawable.computer};

        ArrayList<Card> cards = new ArrayList<Card>();

        Card card = new Card(this, R.layout.row_card);
        // Create a CardHeader
        CardHeader header = new CardHeader(this);
        // Add Header to card
        header.setTitle("शिक्षण");
        card.setTitle("Education");
        card.addCardHeader(header);
        //Add Thumbnail
        CardThumbnail thumb = new CardThumbnail(this);
        thumb.setDrawableResource(listImages[0]);
        card.addCardThumbnail(thumb);
        card.setBackgroundResourceId(R.drawable.demo_card_selector_color1);
        card.setShadow(true);
        cards.add(card);

        card.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = new Intent(MainActivity.this, Education.class);
                startActivity(intent);
            }
        });

        Card card2 = new Card(this, R.layout.row_card);
        // Create a CardHeader
        CardHeader header2 = new CardHeader(this);
        // Add Header to card
        header2.setTitle("कृषि");
        card2.setTitle("Agriculture");
        card2.addCardHeader(header2);
        //Add Thumbnail
        CardThumbnail thumb2 = new CardThumbnail(this);
        thumb2.setDrawableResource(listImages[1]);
        card2.addCardThumbnail(thumb2);
        card2.setBackgroundResourceId(R.drawable.demo_card_selector_color2);
        card2.setShadow(true);
        cards.add(card2);

        card2.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("option",2);
                startActivity(intent);
            }
        });

        Card card3 = new Card(this, R.layout.row_card);
        // Create a CardHeader
        CardHeader header3 = new CardHeader(this);
        // Add Header to card
        header3.setTitle("अंग्रेज़ी");
        card3.setTitle("English");
        card3.addCardHeader(header3);
        //Add Thumbnail
        CardThumbnail thumb3 = new CardThumbnail(this);
        thumb3.setDrawableResource(listImages[2]);
        card3.addCardThumbnail(thumb3);
        card3.setBackgroundResourceId(R.drawable.demo_card_selector_color3);
        card3.setShadow(true);
        cards.add(card3);

        card3.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("option",3);
                startActivity(intent);
            }
        });

        Card card4 = new Card(this, R.layout.row_card);
        // Create a CardHeader
        CardHeader header4 = new CardHeader(this);
        // Add Header to card
        header4.setTitle("कंप्यूटर");
        card4.setTitle("Computer");
        card4.addCardHeader(header4);
        //Add Thumbnail
        CardThumbnail thumb4 = new CardThumbnail(this);
        thumb4.setDrawableResource(listImages[3]);
        card4.addCardThumbnail(thumb4);
        card4.setBackgroundResourceId(R.drawable.demo_card_selector_color4);
        card4.setShadow(true);
        cards.add(card4);

        card4.setOnClickListener(new Card.OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("option",4);
                startActivity(intent);
            }
        });

        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);

        CardListView listView = (CardListView) this.findViewById(R.id.myList);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }

    }

}
