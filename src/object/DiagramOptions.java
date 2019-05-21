//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.04.17 at 08:40:10 PM ICT 
//


package object;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for diagramOptions complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="diagramOptions">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded">
 *         &lt;element ref="{http://www.visual-paradigm.com/product/vpuml/modelexporter}BooleanProperty" minOccurs="0"/>
 *         &lt;element ref="{http://www.visual-paradigm.com/product/vpuml/modelexporter}IntegerProperty" minOccurs="0"/>
 *         &lt;element ref="{http://www.visual-paradigm.com/product/vpuml/modelexporter}StringProperty" minOccurs="0"/>
 *         &lt;element ref="{http://www.visual-paradigm.com/product/vpuml/modelexporter}ColorProperty" minOccurs="0"/>
 *         &lt;element name="defaultHtmlDocFont" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="family" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="size" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *                 &lt;attribute name="style" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "diagramOptions", propOrder = {
    "booleanPropertyOrIntegerPropertyOrStringProperty"
})
public class DiagramOptions {

    @XmlElements({
        @XmlElement(name = "BooleanProperty", type = BooleanProperty.class),
        @XmlElement(name = "IntegerProperty", type = IntegerProperty.class),
        @XmlElement(name = "StringProperty", type = StringProperty.class),
        @XmlElement(name = "ColorProperty", type = ColorProperty.class),
        @XmlElement(name = "defaultHtmlDocFont", type = DiagramOptions.DefaultHtmlDocFont.class)
    })
    protected List<Object> booleanPropertyOrIntegerPropertyOrStringProperty;

    /**
     * Gets the value of the booleanPropertyOrIntegerPropertyOrStringProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the booleanPropertyOrIntegerPropertyOrStringProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBooleanPropertyOrIntegerPropertyOrStringProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BooleanProperty }
     * {@link IntegerProperty }
     * {@link StringProperty }
     * {@link ColorProperty }
     * {@link DiagramOptions.DefaultHtmlDocFont }
     * 
     * 
     */
    public List<Object> getBooleanPropertyOrIntegerPropertyOrStringProperty() {
        if (booleanPropertyOrIntegerPropertyOrStringProperty == null) {
            booleanPropertyOrIntegerPropertyOrStringProperty = new ArrayList<Object>();
        }
        return this.booleanPropertyOrIntegerPropertyOrStringProperty;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="family" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="size" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *       &lt;attribute name="style" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DefaultHtmlDocFont {

        @XmlAttribute(name = "family")
        protected String family;
        @XmlAttribute(name = "size")
        protected BigInteger size;
        @XmlAttribute(name = "style")
        protected BigInteger style;

        /**
         * Gets the value of the family property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFamily() {
            return family;
        }

        /**
         * Sets the value of the family property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFamily(String value) {
            this.family = value;
        }

        /**
         * Gets the value of the size property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getSize() {
            return size;
        }

        /**
         * Sets the value of the size property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setSize(BigInteger value) {
            this.size = value;
        }

        /**
         * Gets the value of the style property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getStyle() {
            return style;
        }

        /**
         * Sets the value of the style property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setStyle(BigInteger value) {
            this.style = value;
        }

    }

}
